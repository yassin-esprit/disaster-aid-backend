package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Achievement;
import tn.esprit.disasteraidbackend.modules.gamification.entity.VolunteerAchievement;
import tn.esprit.disasteraidbackend.modules.gamification.repository.AchievementRepository;
import tn.esprit.disasteraidbackend.modules.gamification.repository.VolunteerAchievementRepository;
import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoBadgeServiceImpl implements AutoBadgeService {

    private final AchievementRepository          achievementRepository;
    private final VolunteerAchievementRepository  volunteerAchievementRepository;

    @Override
    public void checkAndAwardBadges(ImpactRecord record) {
        Long   volunteerId = record.getVolunteerId();
        double score       = record.getImpactScore() != null ? record.getImpactScore() : 0.0;

        List<Achievement> activeAchievements = achievementRepository.findByIsActive(true);

        for (Achievement achievement : activeAchievements) {

            String criteria = achievement.getCriteria();
            if (criteria == null || criteria.isBlank()) continue;

            // Parse threshold without Jackson — handles {"min_impact_score": 50}
            double threshold = parseMinImpactScore(criteria, achievement.getAchievementId());
            if (threshold <= 0) continue;

            if (score < threshold) continue;

            // Skip if volunteer already has this badge
            boolean alreadyHas = volunteerAchievementRepository
                    .existsByVolunteerIdAndAchievementId(volunteerId, achievement.getAchievementId());
            if (alreadyHas) continue;

            // ✅ Award the badge
            VolunteerAchievement badge = VolunteerAchievement.builder()
                    .volunteerId(volunteerId)
                    .achievementId(achievement.getAchievementId())
                    .earnedAt(LocalDateTime.now())
                    .isDisplayed(true)
                    .build();

            volunteerAchievementRepository.save(badge);

            log.info("🏅 AUTO-BADGE: Volunteer {} earned '{}' (score={} ≥ threshold={})",
                    volunteerId, achievement.getName(), score, threshold);
        }
    }

    // ── Simple string parser — no Jackson needed ──────────────────────────────
    // Handles: {"min_impact_score": 50} or {"min_impact_score":50}

    private double parseMinImpactScore(String criteria, Long achievementId) {
        try {
            // Look for "min_impact_score" key in the JSON string
            String key = "min_impact_score";
            int keyIndex = criteria.indexOf(key);
            if (keyIndex == -1) return -1; // key not present — not an auto-award achievement

            // Find the colon after the key, then grab the number after it
            int colonIndex = criteria.indexOf(':', keyIndex);
            if (colonIndex == -1) return -1;

            // Extract everything after the colon, strip spaces and closing braces
            String afterColon = criteria.substring(colonIndex + 1)
                    .replaceAll("[^0-9.]", " ")
                    .trim();

            // Take the first token (the number)
            String[] parts = afterColon.split("\\s+");
            if (parts.length == 0 || parts[0].isBlank()) return -1;

            return Double.parseDouble(parts[0]);

        } catch (Exception e) {
            log.warn("AutoBadgeService — could not parse criteria for achievement {}: '{}'",
                    achievementId, criteria);
            return -1;
        }
    }
}
