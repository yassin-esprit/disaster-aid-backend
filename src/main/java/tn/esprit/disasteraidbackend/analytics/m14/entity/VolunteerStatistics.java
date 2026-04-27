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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Integer statId;

    @Column(nullable = false)
    private Integer volunteerId;

    @Column(nullable = false)
    private Double totalApprovedHours;

    @Column(nullable = false)
    private Integer totalOpportunities;

    @Column(columnDefinition = "TEXT")
    private String causeDistribution;

    @Column(columnDefinition = "TEXT")
    private String skillsUtilised;

    private Double avgRating;

    @Column(nullable = false)
    private Integer currentStreak;

    @Column(nullable = false)
    private Integer longestStreak;

    @Column(nullable = false)
    private Integer achievementCount;

    private LocalDate lastActivityDate;

    private Double churnProbability;

    @Column(nullable = false)
    private LocalDateTime computedAt;

    @PrePersist
    void prePersist() {
        if (computedAt == null) {
            computedAt = LocalDateTime.now();
        }
        if (totalApprovedHours == null) {
            totalApprovedHours = 0D;
        }
        if (totalOpportunities == null) {
            totalOpportunities = 0;
        }
        if (currentStreak == null) {
            currentStreak = 0;
        }
        if (longestStreak == null) {
            longestStreak = 0;
        }
        if (achievementCount == null) {
            achievementCount = 0;
        }
    }
}

