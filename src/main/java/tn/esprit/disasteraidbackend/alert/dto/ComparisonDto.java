package tn.esprit.disasteraidbackend.alert.dto;

import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;

public class ComparisonDto {

    private AlertSide alertA;
    private AlertSide alertB;

    public ComparisonDto(Alert a, DisasterStatistics statsA, Alert b, DisasterStatistics statsB) {
        this.alertA = new AlertSide(a, statsA);
        this.alertB = new AlertSide(b, statsB);
    }

    public AlertSide getAlertA() { return alertA; }
    public AlertSide getAlertB() { return alertB; }

    public static class AlertSide {
        private Alert alert;
        private DisasterStatistics stats;

        public AlertSide(Alert alert, DisasterStatistics stats) {
            this.alert = alert;
            this.stats = stats;
        }

        public Alert getAlert() { return alert; }
        public DisasterStatistics getStats() { return stats; }
    }
}
