package tn.esprit.disasteraidbackend.modules.gamification.service;

import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.AchievementCategory;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Rarity;

import java.util.List;

public interface AchievementService {

    AchievementResponse createAchievement(AchievementRequest request);

    AchievementResponse getAchievementById(Long id);

    List<AchievementResponse> getAllAchievements();

    List<AchievementResponse> getByCategory(AchievementCategory category);

    List<AchievementResponse> getByRarity(Rarity rarity);

    List<AchievementResponse> getActiveAchievements();

    AchievementResponse updateAchievement(Long id, AchievementRequest request);

    void deleteAchievement(Long id);
}
