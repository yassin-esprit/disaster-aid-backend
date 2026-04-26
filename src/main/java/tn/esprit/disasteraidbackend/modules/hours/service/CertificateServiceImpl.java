package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateResponse;
import tn.esprit.disasteraidbackend.modules.hours.entity.Certificate;
import tn.esprit.disasteraidbackend.modules.hours.repository.CertificateRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private Certificate toEntity(CertificateRequest request) {
        return Certificate.builder()
                .volunteerId(request.getVolunteerId())
                .orgId(request.getOrgId())
                .totalHours(request.getTotalHours())
                .periodFrom(request.getPeriodFrom())
                .periodTo(request.getPeriodTo())
                .skills(request.getSkills())
                .pdfUrl(request.getPdfUrl())
                .impactCardUrl(request.getImpactCardUrl())
                .credentialHash(request.getCredentialHash())
                .issuedAt(LocalDateTime.now())
                .expiresAt(request.getExpiresAt())
                .build();
    }

    private CertificateResponse toResponse(Certificate c) {
        CertificateResponse response = new CertificateResponse();
        response.setCertificateId(c.getCertificateId());
        response.setVolunteerId(c.getVolunteerId());
        response.setOrgId(c.getOrgId());
        response.setTotalHours(c.getTotalHours());
        response.setPeriodFrom(c.getPeriodFrom());
        response.setPeriodTo(c.getPeriodTo());
        response.setSkills(c.getSkills());
        response.setPdfUrl(c.getPdfUrl());
        response.setImpactCardUrl(c.getImpactCardUrl());
        response.setCredentialHash(c.getCredentialHash());
        response.setIssuedAt(c.getIssuedAt());
        response.setExpiresAt(c.getExpiresAt());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public CertificateResponse createCertificate(CertificateRequest request) {
        Certificate saved = certificateRepository.save(toEntity(request));
        return toResponse(saved);
    }

    @Override
    public CertificateResponse getCertificateById(Long id) {
        Certificate c = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found with id: " + id));
        return toResponse(c);
    }

    @Override
    public List<CertificateResponse> getAllCertificates() {
        return certificateRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CertificateResponse> getCertificatesByVolunteer(Long volunteerId) {
        return certificateRepository.findByVolunteerId(volunteerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CertificateResponse> getCertificatesByOrg(Long orgId) {
        return certificateRepository.findByOrgId(orgId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CertificateResponse updateCertificate(Long id, CertificateRequest request) {
        Certificate c = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found with id: " + id));
        c.setVolunteerId(request.getVolunteerId());
        c.setOrgId(request.getOrgId());
        c.setTotalHours(request.getTotalHours());
        c.setPeriodFrom(request.getPeriodFrom());
        c.setPeriodTo(request.getPeriodTo());
        c.setSkills(request.getSkills());
        c.setPdfUrl(request.getPdfUrl());
        c.setImpactCardUrl(request.getImpactCardUrl());
        c.setCredentialHash(request.getCredentialHash());
        c.setExpiresAt(request.getExpiresAt());
        return toResponse(certificateRepository.save(c));
    }

    @Override
    public void deleteCertificate(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new RuntimeException("Certificate not found with id: " + id);
        }
        certificateRepository.deleteById(id);
    }
}
