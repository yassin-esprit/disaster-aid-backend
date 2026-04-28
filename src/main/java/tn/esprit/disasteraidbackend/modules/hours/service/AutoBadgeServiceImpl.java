package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final AchievementRepository         achievementRepository;
    private final VolunteerAchievementRepository volunteerAchievementRepository;

    @Override
    @Transactional  // ← ensures a Hibernate session is open for all DB calls inside
    public void checkAndAwardBadges(ImpactRecord record) {
        Long   volunteerId = record.getVolunteerId();
        double score       = record.getImpactScore() != null ? record.getImpactScore() : 0.0;

        log.info("AutoBadgeService — checking badges for volunteer {} with score {}", volunteerId, score);

        List<Achievement> activeAchievements;
        try {
            activeAchievements = achievementRepository.findByIsActive(true);
        } catch (Exception e) {
            log.error("AutoBadgeService — failed to load achievements: {}", e.getMessage());
            return;
        }

        log.info("AutoBadgeService — found {} active achievements to check", activeAchievements.size());

        for (Achievement achievement : activeAchievements) {
            try {
                String criteria = achievement.getCriteria();
                if (criteria == null || criteria.isBlank()) {
                    log.debug("AutoBadgeService — achievement '{}' has no criteria, skipping", achievement.getName());
                    continue;
                }

                double threshold = parseMinImpactScore(criteria, achievement.getAchievementId());
                if (threshold <= 0) {
                    log.debug("AutoBadgeService — achievement '{}' has no min_impact_score, skipping", achievement.getName());
                    continue;
                }

                log.info("AutoBadgeService — checking '{}': score {} vs threshold {}", achievement.getName(), score, threshold);

                if (score < threshold) {
                    log.info("AutoBadgeService — score {} < threshold {}, not eligible", score, threshold);
                    continue;
                }

                boolean alreadyHas = volunteerAchievementRepository
                        .existsByVolunteerIdAndAchievementId(volunteerId, achievement.getAchievementId());

                if (alreadyHas) {
                    log.info("AutoBadgeService — volunteer {} already has '{}', skipping", volunteerId, achievement.getName());
                    continue;
                }

                // ✅ Award the badge
                VolunteerAchievement badge = VolunteerAchievement.builder()
                        .volunteerId(volunteerId)
                        .achievementId(achievement.getAchievementId())
                        .earnedAt(LocalDateTime.now())
                        .isDisplayed(true)
                        .build();

                volunteerAchievementRepository.save(badge);

                log.info("🏅 AUTO-BADGE AWARDED: Volunteer {} earned '{}' (score={} ≥ threshold={})",
                        volunteerId, achievement.getName(), score, threshold);

            } catch (Exception e) {
                log.error("AutoBadgeService — error processing achievement '{}' for volunteer {}: {}",
                        achievement.getName(), volunteerId, e.getMessage());
            }
        }
    }

    // ── Manual JSON parser — no Jackson needed ────────────────────────────────
    // Handles: {"min_impact_score": 50} or {"min_impact_score":50}

    private double parseMinImpactScore(String criteria, Long achievementId) {
        try {
            String key = "min_impact_score";
            int keyIndex = criteria.indexOf(key);
            if (keyIndex == -1) return -1;

            int colonIndex = criteria.indexOf(':', keyIndex);
            if (colonIndex == -1) return -1;

            String afterColon = criteria.substring(colonIndex + 1)
                    .replaceAll("[^0-9.]", " ")
                    .trim();

            String[] parts = afterColon.split("\\s+");
            if (parts.length == 0 || parts[0].isBlank()) return -1;

            double result = Double.parseDouble(parts[0]);
            log.debug("AutoBadgeService — parsed threshold {} for achievement {}", result, achievementId);
            return result;

        } catch (Exception e) {
            log.warn("AutoBadgeService — could not parse criteria for achievement {}: '{}'",
                    achievementId, criteria);
            return -1;
        }
    }
}
