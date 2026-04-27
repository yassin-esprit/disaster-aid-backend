package tn.esprit.disasteraidbackend.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sla_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alert_id", nullable = false)
    private Integer alertId;

    @Column(nullable = false)
    private LocalDateTime incidentAt;

    private LocalDateTime acknowledgedAt;

    private LocalDateTime resolvedAt;

    /** Minutes from incident to acknowledgement */
    private Double timeToAcknowledgeMin;

    /** Minutes from acknowledgement to resolution */
    private Double timeToResolveMin;

    /** Total minutes from incident to resolution */
    private Double totalResolutionMin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlaStatus slaStatus;

    public enum SlaStatus {
        WITHIN_SLA, AT_RISK, BREACHED
    }

    @PrePersist
    void prePersist() {
        if (incidentAt == null) incidentAt = LocalDateTime.now();
        if (slaStatus == null) slaStatus = SlaStatus.WITHIN_SLA;
        computeDelays();
    }

    @PreUpdate
    void preUpdate() {
        computeDelays();
    }

    private void computeDelays() {
        if (incidentAt != null && acknowledgedAt != null) {
            timeToAcknowledgeMin = java.time.Duration.between(incidentAt, acknowledgedAt).toMinutes() / 1.0;
        }
        if (acknowledgedAt != null && resolvedAt != null) {
            timeToResolveMin = java.time.Duration.between(acknowledgedAt, resolvedAt).toMinutes() / 1.0;
        }
        if (incidentAt != null && resolvedAt != null) {
            totalResolutionMin = java.time.Duration.between(incidentAt, resolvedAt).toMinutes() / 1.0;
        }
    }
}
