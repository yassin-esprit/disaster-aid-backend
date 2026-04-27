package tn.esprit.disasteraidbackend.modules.hours.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.hours.entity.ConfidenceLevel;

import java.time.LocalDateTime;

@Data
public class ImpactRecordResponse {
    private Long impactId;
    private Long volunteerId;
    private Float totalApprovedHours;
    private Float co2OffsetKg;
    private Integer mealsServed;
    private Integer peopleHelped;
    private Float wasteRemovedKg;
    private Float hoursOfTutoring;
    private ConfidenceLevel confidenceLevel;

    // ── Calculated automatically by ImpactScoreScheduler ─────────────────
    private Double impactScore;

    private LocalDateTime lastCalculatedAt;
}
