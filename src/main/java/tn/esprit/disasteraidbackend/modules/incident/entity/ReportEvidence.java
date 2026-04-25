package tn.esprit.disasteraidbackend.modules.incident.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "report_evidences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportEvidence extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_report_id", nullable = false)
    private IncidentReport incidentReport;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;   // IMAGE, VIDEO, AUDIO

    @Column(name = "clip_score")
    private Double clipScore = 0.0;

    @Column(name = "captured_at")
    private java.time.LocalDateTime capturedAt;
}