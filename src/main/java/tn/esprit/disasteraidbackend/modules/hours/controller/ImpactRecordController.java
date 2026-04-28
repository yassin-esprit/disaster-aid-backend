package tn.esprit.disasteraidbackend.modules.hours.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordResponse;
import tn.esprit.disasteraidbackend.modules.hours.service.ImpactRecordService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/impact-records")
@RequiredArgsConstructor
public class ImpactRecordController {

    private final ImpactRecordService impactRecordService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ImpactRecordResponse> createImpactRecord(@RequestBody ImpactRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(impactRecordService.createImpactRecord(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ImpactRecordResponse>> getAllImpactRecords() {
        return ResponseEntity.ok(impactRecordService.getAllImpactRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImpactRecordResponse> getImpactRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(impactRecordService.getImpactRecordById(id));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<ImpactRecordResponse> getByVolunteer(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(impactRecordService.getImpactRecordByVolunteer(volunteerId));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ImpactRecordResponse> updateImpactRecord(
            @PathVariable Long id,
            @RequestBody ImpactRecordRequest request) {
        return ResponseEntity.ok(impactRecordService.updateImpactRecord(id, request));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImpactRecord(@PathVariable Long id) {
        impactRecordService.deleteImpactRecord(id);
        return ResponseEntity.noContent().build();
    }
}
