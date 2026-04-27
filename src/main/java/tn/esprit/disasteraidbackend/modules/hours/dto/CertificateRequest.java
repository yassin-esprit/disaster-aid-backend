package tn.esprit.disasteraidbackend.modules.hours.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CertificateRequest {
    private Long volunteerId;
    private Long orgId;
    private Float totalHours;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private List<Integer> skills;
    private String pdfUrl;
    private String impactCardUrl;
    private String credentialHash;
    private LocalDate expiresAt;
}
