package tn.esprit.disasteraidbackend.modules.incident.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "incident_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReport extends BaseEntity {

    // Temporary plain Integer — will become @ManyToOne User once Student 1 merges
    @Column(name = "reporter_id")
    private Integer reporterId;

    // Temporary plain Integer — will become @ManyToOne Alert once Student 2 merges
    @Column(name = "alert_id")
    private Integer alertId;

    @Column(nullable = false)
    private String disasterType;   // FLOOD, EARTHQUAKE, WILDFIRE, etc.

    @Column(nullable = false)
    private String severity;       // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false)
    private String location;       // simplified — GeoPoint comes later

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status = "PENDING";   // PENDING, VERIFIED, REJECTED, ESCALATED

    @Column(name = "credibility_score")
    private Double credibilityScore = 0.0;

    @Column(name = "upvote_count")
    private Integer upvoteCount = 0;

     @OneToMany(mappedBy = "incidentReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ReportEvidence> evidences = new java.util.ArrayList<>();
}