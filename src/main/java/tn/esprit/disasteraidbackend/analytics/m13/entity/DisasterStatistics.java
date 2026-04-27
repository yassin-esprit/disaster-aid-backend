package tn.esprit.disasteraidbackend.analytics.m13.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "disaster_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisasterStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Integer statId;

    @Column(name = "alert_id", nullable = false)
    private Integer alertId;

    @Column(nullable = false)
    private Integer totalReports;

    @Column(nullable = false)
    private Integer verifiedReports;

    @Column(nullable = false)
    private Integer totalHelpRequests;

    @Column(nullable = false)
    private Integer resolvedRequests;

    @Column(nullable = false)
    private Integer activeResponders;

    @Column(columnDefinition = "TEXT")
    private String shelterOccupancy;

    private Double avgResponseTimeMin;

    private Double affectedAreaKm2;

    private Double credibilityWeightedCount;

    @Column(nullable = false)
    private LocalDateTime computedAt;

    @PrePersist
    void prePersist() {
        if (computedAt == null) {
            computedAt = LocalDateTime.now();
        }
        if (totalReports == null) {
            totalReports = 0;
        }
        if (verifiedReports == null) {
            verifiedReports = 0;
        }
        if (totalHelpRequests == null) {
            totalHelpRequests = 0;
        }
        if (resolvedRequests == null) {
            resolvedRequests = 0;
        }
        if (activeResponders == null) {
            activeResponders = 0;
        }
    }
}

