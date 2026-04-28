package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.hours.entity.ConfidenceLevel;
import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;
import tn.esprit.disasteraidbackend.modules.hours.repository.ImpactRecordRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImpactScoreServiceImpl implements ImpactScoreService {

    private final ImpactRecordRepository impactRecordRepository;
    private final AutoBadgeService       autoBadgeService;

    // ── Scoring weights ───────────────────────────────────────────────────────
    private static final double WEIGHT_HOURS    = 10.0;
    private static final double WEIGHT_TUTORING = 8.0;
    private static final double WEIGHT_CO2      = 5.0;
    private static final double WEIGHT_WASTE    = 3.0;
    private static final double WEIGHT_PEOPLE   = 2.0;
    private static final double WEIGHT_MEALS    = 1.0;

    // ── Confidence multipliers ────────────────────────────────────────────────
    private static final double MULTIPLIER_VERIFIED   = 1.0;
    private static final double MULTIPLIER_CALIBRATED = 0.8;
    private static final double MULTIPLIER_ESTIMATED  = 0.5;

    @Override
    @Transactional
    public ImpactRecord calculateForRecord(ImpactRecord record) {

        double hours    = record.getTotalApprovedHours() != null ? record.getTotalApprovedHours() : 0;
        double tutoring = record.getHoursOfTutoring()    != null ? record.getHoursOfTutoring()    : 0;
        double co2      = record.getCo2OffsetKg()         != null ? record.getCo2OffsetKg()         : 0;
        double waste    = record.getWasteRemovedKg()      != null ? record.getWasteRemovedKg()      : 0;
        double people   = record.getPeopleHelped()         != null ? record.getPeopleHelped()         : 0;
        double meals    = record.getMealsServed()          != null ? record.getMealsServed()          : 0;

        double rawScore =
                (hours    * WEIGHT_HOURS)    +
                (tutoring * WEIGHT_TUTORING) +
                (co2      * WEIGHT_CO2)      +
                (waste    * WEIGHT_WASTE)    +
                (people   * WEIGHT_PEOPLE)   +
                (meals    * WEIGHT_MEALS);

        double multiplier = getConfidenceMultiplier(record.getConfidenceLevel());
        double finalScore = Math.round(rawScore * multiplier * 100.0) / 100.0;

        record.setImpactScore(finalScore);
        record.setLastCalculatedAt(LocalDateTime.now());

        ImpactRecord saved = impactRecordRepository.save(record);

        log.info("ImpactScore — volunteer {} → score {}", record.getVolunteerId(), finalScore);

        return saved;
    }

    @Override
    public void recalculateAll() {
        List<ImpactRecord> all = impactRecordRepository.findAll();
        log.info("ImpactScoreScheduler — recalculating {} records…", all.size());

        int scoreUpdated = 0;
        int badgesChecked = 0;

        for (ImpactRecord record : all) {

            // Step 1 — calculate and save score (own try/catch)
            ImpactRecord saved = null;
            try {
                saved = calculateForRecord(record);
                scoreUpdated++;
            } catch (Exception e) {
                log.error("ImpactScore — failed to calculate score for volunteerId={}: {}",
                        record.getVolunteerId(), e.getMessage());
                continue; // skip badge check if score failed
            }

            // Step 2 — check and award badges (separate try/catch — badge errors won't affect score)
            try {
                autoBadgeService.checkAndAwardBadges(saved);
                badgesChecked++;
            } catch (Exception e) {
                log.error("AutoBadge — failed badge check for volunteerId={}: {}",
                        saved.getVolunteerId(), e.getMessage(), e);
            }
        }

        log.info("ImpactScoreScheduler — done. Scores updated: {}/{}, Badge checks: {}/{}",
                scoreUpdated, all.size(), badgesChecked, all.size());
    }

    private double getConfidenceMultiplier(ConfidenceLevel level) {
        if (level == null) return MULTIPLIER_ESTIMATED;
        return switch (level) {
            case VERIFIED   -> MULTIPLIER_VERIFIED;
            case UNVERIFIED -> MULTIPLIER_CALIBRATED;
            case ESTIMATED  -> MULTIPLIER_ESTIMATED;
        };
    }
}
