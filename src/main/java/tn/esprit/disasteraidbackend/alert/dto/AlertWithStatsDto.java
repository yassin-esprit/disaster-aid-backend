package tn.esprit.disasteraidbackend.alert.dto;

import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;

public class AlertWithStatsDto {

    private final Alert alert;
    private final DisasterStatistics latestStats;

    public AlertWithStatsDto(Alert alert, DisasterStatistics latestStats) {
        this.alert = alert;
        this.latestStats = latestStats;
    }

    public Alert getAlert() {
        return alert;
    }

    public DisasterStatistics getLatestStats() {
        return latestStats;
    }
}
