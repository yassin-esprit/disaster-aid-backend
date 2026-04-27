package tn.esprit.disasteraidbackend.modules.hours.service;

import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;

public interface AutoBadgeService {

    /**
     * Called after an ImpactRecord score is calculated.
     * Checks ALL active achievements whose criteria contains
     * "min_impact_score" and awards any that the volunteer
     * now qualifies for (and doesn't already have).
     */
    void checkAndAwardBadges(ImpactRecord record);
}
