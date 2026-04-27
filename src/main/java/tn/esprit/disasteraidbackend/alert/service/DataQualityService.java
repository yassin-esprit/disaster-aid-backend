package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.dto.DataQualityReportDto;
import tn.esprit.disasteraidbackend.alert.dto.DataQualityReportDto.DataIssue;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;
import tn.esprit.disasteraidbackend.alert.repository.AlertRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataQualityService {

    private final AlertRepository alertRepo;
    private final DisasterStatisticsRepository statsRepo;

    public DataQualityService(AlertRepository alertRepo, DisasterStatisticsRepository statsRepo) {
        this.alertRepo = alertRepo;
        this.statsRepo = statsRepo;
    }

    public DataQualityReportDto analyze() {
        List<Alert> allAlerts = alertRepo.findAll();
        List<DataIssue> issues = new ArrayList<>();

        for (Alert alert : allAlerts) {
            // Check missing location
            if (alert.getLatitude() == null || alert.getLongitude() == null
                    || alert.getLatitude() == 0.0 || alert.getLongitude() == 0.0) {
                issues.add(new DataIssue("CRITICAL", "MISSING_LOCATION",
                        "Alert has no valid coordinates", alert.getAlertId(), alert.getTitle()));
            }

            // Check missing region
            if (alert.getRegion() == null || alert.getRegion().isBlank()) {
                issues.add(new DataIssue("WARNING", "MISSING_REGION",
                        "Alert has no region specified", alert.getAlertId(), alert.getTitle()));
            }

            // Check unclosed events (ACTIVE for more than 30 days)
            if (alert.getStatus() == AlertStatus.ACTIVE
                    && alert.getReportedAt().isBefore(LocalDateTime.now().minusDays(30))) {
                issues.add(new DataIssue("WARNING", "STALE_ACTIVE",
                        "Alert has been ACTIVE for more than 30 days without resolution",
                        alert.getAlertId(), alert.getTitle()));
            }

            // Check resolved without resolvedAt timestamp
            if (alert.getStatus() == AlertStatus.RESOLVED && alert.getResolvedAt() == null) {
                issues.add(new DataIssue("WARNING", "MISSING_RESOLVED_DATE",
                        "Alert is RESOLVED but has no resolution timestamp",
                        alert.getAlertId(), alert.getTitle()));
            }

            // Check missing description
            if (alert.getDescription() == null || alert.getDescription().isBlank()) {
                issues.add(new DataIssue("INFO", "MISSING_DESCRIPTION",
                        "Alert has no description", alert.getAlertId(), alert.getTitle()));
            }

            // Check radius sanity
            if (alert.getRadiusKm() != null && alert.getRadiusKm() > 200) {
                issues.add(new DataIssue("WARNING", "LARGE_RADIUS",
                        "Alert radius exceeds 200km — possibly incorrect",
                        alert.getAlertId(), alert.getTitle()));
            }

            // Check for missing statistics
            boolean hasStats = statsRepo.findTopByAlertIdOrderByComputedAtDesc(alert.getAlertId()).isPresent();
            if (!hasStats && alert.getStatus() != AlertStatus.RESOLVED) {
                issues.add(new DataIssue("INFO", "NO_STATISTICS",
                        "Active alert has no disaster statistics data",
                        alert.getAlertId(), alert.getTitle()));
            }
        }

        int total = allAlerts.size();
        int issueCount = issues.size();
        double score = total == 0 ? 100.0 : Math.max(0, 100.0 - (issueCount * 100.0 / (total * 7.0)) * 100.0);
        score = Math.min(100.0, Math.max(0, score));

        return new DataQualityReportDto(total, issueCount, Math.round(score * 10) / 10.0, issues);
    }
}
