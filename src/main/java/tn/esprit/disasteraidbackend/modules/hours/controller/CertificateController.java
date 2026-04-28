package tn.esprit.disasteraidbackend.modules.hours.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateResponse;
import tn.esprit.disasteraidbackend.modules.hours.service.CertificateService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CertificateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificateService.createCertificate(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<CertificateResponse>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificateById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<CertificateResponse>> getByVolunteer(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(certificateService.getCertificatesByVolunteer(volunteerId));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<CertificateResponse>> getByOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(certificateService.getCertificatesByOrg(orgId));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<CertificateResponse> updateCertificate(
            @PathVariable Long id,
            @RequestBody CertificateRequest request) {
        return ResponseEntity.ok(certificateService.updateCertificate(id, request));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.noContent().build();
    }
}
