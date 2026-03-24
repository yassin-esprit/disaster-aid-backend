package tn.esprit.disasteraidbackend.modules.incident.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.incident.entity.ReportEvidence;
import tn.esprit.disasteraidbackend.modules.incident.service.ReportEvidenceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evidences")
@RequiredArgsConstructor
public class ReportEvidenceController {

    private final ReportEvidenceService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportEvidence>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportEvidence>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<ApiResponse<List<ReportEvidence>>> getByReportId(@PathVariable Integer reportId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByReportId(reportId)));
    }

    @PostMapping("/report/{reportId}")
    public ResponseEntity<ApiResponse<ReportEvidence>> create(
            @PathVariable Integer reportId,
            @RequestBody ReportEvidence evidence) {
        return ResponseEntity.ok(ApiResponse.ok("Evidence added", service.create(reportId, evidence)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
