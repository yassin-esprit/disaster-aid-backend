package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.gamification.entity.ChallengeStatus;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChallengeResponse {
    private Long challengeId;
    private String title;
    private String description;
    private String targetMetric;
    private Float targetValue;
    private Integer rewardPoints;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> participantIds;
    private ChallengeStatus status;
}
