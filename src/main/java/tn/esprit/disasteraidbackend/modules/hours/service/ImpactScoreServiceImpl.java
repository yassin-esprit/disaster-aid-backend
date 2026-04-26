package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final AutoBadgeService autoBadgeService;  // ← injected

    // ── Scoring weights ───────────────────────────────────────────────────────
    private static final double WEIGHT_HOURS    = 10.0;
    private static final double WEIGHT_TUTORING = 8.0;
    private static final double WEIGHT_CO2      = 5.0;
    private static final double WEIGHT_WASTE    = 3.0;
    private static final double WEIGHT_PEOPLE   = 2.0;
    private static final double WEIGHT_MEALS    = 1.0;

    // ── Confidence multipliers ────────────────────────────────────────────────
    private static final double MULTIPLIER_VERIFIED   = 1.0;
    private static final double MULTIPLIER_UNVERIFIED = 0.001;
    private static final double MULTIPLIER_ESTIMATED  = 0.5;

    @Override
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

        log.debug("Volunteer {} → rawScore={} × {} = impactScore={}",
            record.getVolunteerId(), rawScore, multiplier, finalScore);

        // ✅ After saving the score, check if new badges should be awarded
        autoBadgeService.checkAndAwardBadges(saved);

        return saved;
    }

    @Override
    public void recalculateAll() {
        List<ImpactRecord> all = impactRecordRepository.findAll();
        log.info("ImpactScoreScheduler — recalculating {} records…", all.size());

        int updated = 0;
        for (ImpactRecord record : all) {
            try {
                calculateForRecord(record);
                updated++;
            } catch (Exception e) {
                log.error("Failed to calculate score for volunteerId={}: {}",
                    record.getVolunteerId(), e.getMessage());
            }
        }
        log.info("ImpactScoreScheduler — done. {}/{} updated.", updated, all.size());
    }

    private double getConfidenceMultiplier(ConfidenceLevel level) {
        if (level == null) return MULTIPLIER_ESTIMATED;
        return switch (level) {
            case VERIFIED   -> MULTIPLIER_VERIFIED;
            case UNVERIFIED -> MULTIPLIER_UNVERIFIED;
            case ESTIMATED  -> MULTIPLIER_ESTIMATED;
        };
    }
}
