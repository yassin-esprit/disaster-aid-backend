package tn.esprit.disasteraidbackend.modules.hours.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    // FK → User (volunteer)
    private Long volunteerId;

    // FK → User (organisation)
    private Long orgId;

    private Float totalHours;

    private LocalDate periodFrom;

    private LocalDate periodTo;

    // ✅ FIXED: FetchType.EAGER prevents "no Session" lazy loading error
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "certificate_skills",
            joinColumns = @JoinColumn(name = "certificate_id")
    )
    @Column(name = "skill_id")
    private List<Integer> skills;

    // URL pointing to generated PDF in MinIO / file storage
    private String pdfUrl;

    // URL pointing to generated impact card image
    private String impactCardUrl;

    // SHA-256 hash for tamper-resistance verification
    private String credentialHash;

    private LocalDateTime issuedAt;

    private LocalDate expiresAt;
}
