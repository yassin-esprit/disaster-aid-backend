package tn.esprit.disasteraidbackend.reports.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Generates the analytics PDF report by invoking the Python ai_service and
 * streams the resulting PDF back to the caller.
 *
 * Endpoints:
 *   GET /api/reports/analytics/download — runs python -m app.main and returns the PDF
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Value("${ai-service.python-cmd:python}")
    private String pythonCmd;

    /** ai_service folder (relative to the backend working directory). */
    @Value("${ai-service.path:ai_service}")
    private String aiServicePath;

    /** Where the Python script writes PDFs. Resolved against ai-service.path. */
    @Value("${ai-service.output-dir:ai_reports}")
    private String outputDir;

    /** How long to wait for the Python script before timing out. */
    @Value("${ai-service.timeout-seconds:120}")
    private long timeoutSeconds;

    @GetMapping("/analytics/download")
    public ResponseEntity<byte[]> downloadAnalyticsReport() {
        Path serviceDir = Paths.get(aiServicePath).toAbsolutePath();
        if (!Files.isDirectory(serviceDir)) {
            return error("ai_service directory not found at " + serviceDir);
        }

        Path reportsDir = serviceDir.resolve(outputDir);

        // Run python -m app.main from inside ai_service/
        ProcessBuilder pb = new ProcessBuilder(pythonCmd, "-m", "app.main")
                .directory(serviceDir.toFile())
                .redirectErrorStream(true);
        pb.environment().put("OUTPUT_DIR", outputDir);

        StringBuilder output = new StringBuilder();
        int exitCode;
        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                    if (output.length() > 64_000) {
                        break; // prevent runaway logs
                    }
                }
            }
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return error("Report generation timed out after "
                        + Duration.ofSeconds(timeoutSeconds) + ". Logs:\n" + output);
            }
            exitCode = process.exitValue();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return error("Failed to run ai_service: " + e.getMessage());
        }

        if (exitCode != 0) {
            return error("ai_service exited with status " + exitCode + ":\n" + output);
        }

        Optional<Path> latest = findLatestPdf(reportsDir);
        if (latest.isEmpty()) {
            return error("No PDF found in " + reportsDir + " after generation. Logs:\n" + output);
        }

        try {
            byte[] bytes = Files.readAllBytes(latest.get());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    latest.get().getFileName().toString());
            headers.setContentLength(bytes.length);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return error("Failed to read generated PDF: " + e.getMessage());
        }
    }

    private Optional<Path> findLatestPdf(Path dir) {
        if (!Files.isDirectory(dir)) {
            return Optional.empty();
        }
        try {
            return Files.list(dir)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".pdf"))
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private ResponseEntity<byte[]> error(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(
                message.getBytes(StandardCharsets.UTF_8),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
