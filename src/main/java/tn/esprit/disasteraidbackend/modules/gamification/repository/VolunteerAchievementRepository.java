package tn.esprit.disasteraidbackend.modules.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.gamification.entity.VolunteerAchievement;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerAchievementRepository extends JpaRepository<VolunteerAchievement, Long> {

    List<VolunteerAchievement> findByVolunteerId(Long volunteerId);

    List<VolunteerAchievement> findByAchievementId(Long achievementId);

    List<VolunteerAchievement> findByVolunteerIdAndIsDisplayed(Long volunteerId, Boolean isDisplayed);

    Optional<VolunteerAchievement> findByVolunteerIdAndAchievementId(Long volunteerId, Long achievementId);

    boolean existsByVolunteerIdAndAchievementId(Long volunteerId, Long achievementId);
}
