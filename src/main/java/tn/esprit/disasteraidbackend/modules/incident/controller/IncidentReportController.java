package tn.esprit.disasteraidbackend.modules.incident.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportRequestDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportResponseDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentStatusHistoryDto;
import tn.esprit.disasteraidbackend.modules.incident.service.IncidentReportService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor


public class IncidentReportController {

    private final IncidentReportService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentReportResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> create(
            @RequestBody IncidentReportRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Incident report created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> update(
            @PathVariable Integer id, @RequestBody IncidentReportRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Incident report updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<IncidentReportResponseDto>>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<ApiResponse<List<IncidentReportResponseDto>>> getBySeverity(
            @PathVariable String severity) {
        return ResponseEntity.ok(ApiResponse.ok(service.getBySeverity(severity)));
    }

    @PatchMapping("/{id}/upvote")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> upvote(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Upvoted", service.toggleUpvote(id, true)));
    }

    @PatchMapping("/{id}/downvote")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> downvote(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Downvoted", service.toggleUpvote(id, false)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> updateStatus(
            @PathVariable Integer id, @RequestParam String status) {
        IncidentReportRequestDto dto = new IncidentReportRequestDto();
        IncidentReportResponseDto existing = service.getById(id);
        dto.setDisasterType(existing.getDisasterType());
        dto.setSeverity(existing.getSeverity());
        dto.setLocation(existing.getLocation());
        dto.setDescription(existing.getDescription());
        dto.setStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Status updated", service.update(id, dto)));
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<ApiResponse<IncidentReportResponseDto>> checkDuplicate(
            @RequestBody IncidentReportRequestDto dto) {
        return service.findDuplicate(dto)
                .map(existing -> ResponseEntity.ok(ApiResponse.ok("DUPLICATE_FOUND", existing)))
                .orElse(ResponseEntity.ok(ApiResponse.ok("NO_DUPLICATE", null)));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<IncidentStatusHistoryDto>>> getHistory(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getHistory(id)));
    }
}
