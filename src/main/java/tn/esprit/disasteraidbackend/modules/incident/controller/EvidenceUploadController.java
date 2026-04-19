package tn.esprit.disasteraidbackend.modules.incident.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.common.FileStorageService;
import tn.esprit.disasteraidbackend.modules.incident.dto.ReportEvidenceResponseDto;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.entity.ReportEvidence;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;
import tn.esprit.disasteraidbackend.modules.incident.repository.ReportEvidenceRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/evidences/uploads")
@RequiredArgsConstructor
public class EvidenceUploadController {

    private final FileStorageService fileStorageService;
    private final ReportEvidenceRepository evidenceRepository;
    private final IncidentReportRepository reportRepository;

    @PostMapping("/report/{reportId}/upload")
    public ResponseEntity<ApiResponse<ReportEvidenceResponseDto>> upload(
            @PathVariable Integer reportId,
            @RequestParam("file") MultipartFile file) {

        IncidentReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));

        String filename = fileStorageService.store(file);

        String contentType = file.getContentType();
        String fileType = "IMAGE";
        if (contentType != null) {
            if (contentType.startsWith("video")) fileType = "VIDEO";
            else if (contentType.startsWith("audio")) fileType = "AUDIO";
        }

        ReportEvidence evidence = new ReportEvidence();
        evidence.setIncidentReport(report);
        evidence.setFileUrl("/api/v1/evidences/files/" + filename);
        evidence.setFileType(fileType);
        evidence.setClipScore(0.0);
        evidence.setCapturedAt(LocalDateTime.now());
        ReportEvidence saved = evidenceRepository.save(evidence);

        ReportEvidenceResponseDto dto = new ReportEvidenceResponseDto();
        dto.setId(saved.getId());
        dto.setIncidentReportId(reportId);
        dto.setFileUrl(saved.getFileUrl());
        dto.setFileType(saved.getFileType());
        dto.setClipScore(saved.getClipScore());
        dto.setCapturedAt(saved.getCapturedAt());

        return ResponseEntity.ok(ApiResponse.ok("File uploaded", dto));
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path path = fileStorageService.load(filename);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();

            String contentType = "application/octet-stream";
            String lower = filename.toLowerCase();
            if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) contentType = "image/jpeg";
            else if (lower.endsWith(".png"))  contentType = "image/png";
            else if (lower.endsWith(".gif"))  contentType = "image/gif";
            else if (lower.endsWith(".webp")) contentType = "image/webp";
            else if (lower.endsWith(".mp4"))  contentType = "video/mp4";
            else if (lower.endsWith(".mov"))  contentType = "video/quicktime";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<ApiResponse<List<ReportEvidenceResponseDto>>> getByReport(
            @PathVariable Integer reportId) {
        List<ReportEvidenceResponseDto> list = evidenceRepository
                .findByIncidentReportId(reportId)
                .stream()
                .map(e -> {
                    ReportEvidenceResponseDto d = new ReportEvidenceResponseDto();
                    d.setId(e.getId());
                    d.setIncidentReportId(reportId);
                    d.setFileUrl(e.getFileUrl());
                    d.setFileType(e.getFileType());
                    d.setClipScore(e.getClipScore());
                    d.setCapturedAt(e.getCapturedAt());
                    return d;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @DeleteMapping("/api/v1/uploads/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        evidenceRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}