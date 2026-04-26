package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.gamification.entity.AchievementCategory;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Rarity;

@Data
public class AchievementResponse {
    private Long achievementId;
    private String name;
    private String description;
    private String iconUrl;
    private AchievementCategory category;
    private String criteria;
    private Integer points;
    private Rarity rarity;
    private Boolean isActive;
}
