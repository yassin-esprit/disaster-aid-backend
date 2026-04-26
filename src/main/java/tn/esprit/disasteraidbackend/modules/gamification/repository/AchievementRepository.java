package tn.esprit.disasteraidbackend.modules.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Achievement;
import tn.esprit.disasteraidbackend.modules.gamification.entity.AchievementCategory;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Rarity;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findByCategory(AchievementCategory category);

    List<Achievement> findByRarity(Rarity rarity);

    List<Achievement> findByIsActive(Boolean isActive);

    List<Achievement> findByCategoryAndIsActive(AchievementCategory category, Boolean isActive);
}
