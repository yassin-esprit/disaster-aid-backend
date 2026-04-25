package tn.esprit.disasteraidbackend.modules.coordination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoordinationStatsDto {
    private long openRequests;
    private long resolvedToday;
    private long activeAssignments;
    private double avgResponseTimeMin;
    private long pendingIncidents;
    private double avgCredibilityScore;
}