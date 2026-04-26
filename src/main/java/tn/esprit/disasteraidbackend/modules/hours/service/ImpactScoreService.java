package tn.esprit.disasteraidbackend.modules.hours.service;

import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;

public interface ImpactScoreService {

    /**
     * Recalculates the impact score for a single ImpactRecord
     * and saves the result to the database.
     *
     * Score formula (weighted):
     *   totalApprovedHours  × 10  pts/hour
     *   hoursOfTutoring     × 8   pts/hour  (high skill value)
     *   co2OffsetKg         × 5   pts/kg
     *   wasteRemovedKg      × 3   pts/kg
     *   peopleHelped        × 2   pts/person
     *   mealsServed         × 1   pt/meal
     *
     * Confidence multiplier:
     *   VERIFIED   → ×1.0  (full score)
     *   UNVERIFIED → ×0  (0%)
     *   ESTIMATED  → ×0.5  (50% — unconfirmed data)
     */
    ImpactRecord calculateForRecord(ImpactRecord record);

    /**
     * Recalculates impact scores for ALL volunteers.
     * Called by the scheduler every night at midnight.
     */
    void recalculateAll();
}
