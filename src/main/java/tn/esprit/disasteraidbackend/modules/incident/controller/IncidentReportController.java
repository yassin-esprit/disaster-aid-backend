package tn.esprit.disasteraidbackend.modules.incident.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.service.IncidentReportService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentReportController {

    private final IncidentReportService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentReport>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentReport>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IncidentReport>> create(@RequestBody IncidentReport report) {
        return ResponseEntity.ok(ApiResponse.ok("Incident report created", service.create(report)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentReport>> update(
            @PathVariable Integer id,
            @RequestBody IncidentReport report) {
        return ResponseEntity.ok(ApiResponse.ok("Incident report updated", service.update(id, report)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<IncidentReport>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<ApiResponse<List<IncidentReport>>> getBySeverity(@PathVariable String severity) {
        return ResponseEntity.ok(ApiResponse.ok(service.getBySeverity(severity)));
    }
}
