package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VolunteerAchievementResponse {
    private Long vaId;
    private Long volunteerId;
    private Long achievementId;
    private LocalDateTime earnedAt;
    private Boolean isDisplayed;
}
