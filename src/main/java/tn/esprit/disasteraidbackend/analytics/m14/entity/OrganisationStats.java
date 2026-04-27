package tn.esprit.disasteraidbackend.analytics.m14.entity;

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
@Table(name = "organisation_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Integer statId;

    @Column(nullable = false)
    private Integer orgId;

    @Column(nullable = false)
    private Integer totalVolunteers;

    @Column(nullable = false)
    private Integer activeVolunteers;

    @Column(nullable = false)
    private Double totalHoursDelivered;

    private Double retentionRate;

    private Double avgRating;

    @Column(columnDefinition = "TEXT")
    private String skillCoverage;

    @Column(nullable = false)
    private Integer openPositions;

    @Column(nullable = false)
    private LocalDateTime computedAt;

    @PrePersist
    void prePersist() {
        if (computedAt == null) {
            computedAt = LocalDateTime.now();
        }
        if (totalVolunteers == null) {
            totalVolunteers = 0;
        }
        if (activeVolunteers == null) {
            activeVolunteers = 0;
        }
        if (totalHoursDelivered == null) {
            totalHoursDelivered = 0D;
        }
        if (openPositions == null) {
            openPositions = 0;
        }
    }
}

