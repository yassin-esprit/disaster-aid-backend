package tn.esprit.disasteraidbackend.alert.dto;

import java.util.List;

public class DataQualityReportDto {
    private int totalAlerts;
    private int totalIssues;
    private double qualityScore;
    private List<DataIssue> issues;

    public DataQualityReportDto(int totalAlerts, int totalIssues, double qualityScore, List<DataIssue> issues) {
        this.totalAlerts = totalAlerts;
        this.totalIssues = totalIssues;
        this.qualityScore = qualityScore;
        this.issues = issues;
    }

    public int getTotalAlerts() { return totalAlerts; }
    public int getTotalIssues() { return totalIssues; }
    public double getQualityScore() { return qualityScore; }
    public List<DataIssue> getIssues() { return issues; }

    public static class DataIssue {
        private String severity; // CRITICAL, WARNING, INFO
        private String category;
        private String message;
        private Integer alertId;
        private String alertTitle;

        public DataIssue(String severity, String category, String message, Integer alertId, String alertTitle) {
            this.severity = severity;
            this.category = category;
            this.message = message;
            this.alertId = alertId;
            this.alertTitle = alertTitle;
        }

        public String getSeverity() { return severity; }
        public String getCategory() { return category; }
        public String getMessage() { return message; }
        public Integer getAlertId() { return alertId; }
        public String getAlertTitle() { return alertTitle; }
    }
}
