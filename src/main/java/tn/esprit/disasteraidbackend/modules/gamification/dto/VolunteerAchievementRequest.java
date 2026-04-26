package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;

@Data
public class VolunteerAchievementRequest {
    private Long volunteerId;
    private Long achievementId;
    private Boolean isDisplayed;
}
