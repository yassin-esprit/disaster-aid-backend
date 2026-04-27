package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.dto.AlertRequestDto;
import tn.esprit.disasteraidbackend.alert.dto.AlertWithStatsDto;
import tn.esprit.disasteraidbackend.alert.dto.MapSummaryDto;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;
import tn.esprit.disasteraidbackend.alert.repository.AlertRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final DisasterStatisticsRepository statsRepository;
    private final AlertNotificationService notificationService;

    public AlertService(AlertRepository alertRepository,
                        DisasterStatisticsRepository statsRepository,
                        AlertNotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.statsRepository = statsRepository;
        this.notificationService = notificationService;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByStatusInOrderBySeverityAscReportedAtDesc(
                List.of(AlertStatus.ACTIVE, AlertStatus.MONITORING));
    }

    public Optional<Alert> getAlertById(Integer alertId) {
        return alertRepository.findById(alertId);
    }

    public List<AlertWithStatsDto> getAlertsWithStats() {
        List<Alert> alerts = alertRepository.findByStatusInOrderBySeverityAscReportedAtDesc(
                List.of(AlertStatus.ACTIVE, AlertStatus.MONITORING));

        return alerts.stream().map(alert -> {
            DisasterStatistics latestStats = statsRepository
                    .findTopByAlertIdOrderByComputedAtDesc(alert.getAlertId())
                    .orElse(null);
            return new AlertWithStatsDto(alert, latestStats);
        }).toList();
    }

    public Optional<AlertWithStatsDto> getAlertWithStats(Integer alertId) {
        return alertRepository.findById(alertId).map(alert -> {
            DisasterStatistics latestStats = statsRepository
                    .findTopByAlertIdOrderByComputedAtDesc(alertId)
                    .orElse(null);
            return new AlertWithStatsDto(alert, latestStats);
        });
    }

    public MapSummaryDto getMapSummary() {
        long active = alertRepository.countByStatus(AlertStatus.ACTIVE);
        long monitoring = alertRepository.countByStatus(AlertStatus.MONITORING);
        long resolved = alertRepository.countByStatus(AlertStatus.RESOLVED);

        List<DisasterStatistics> allStats = statsRepository.findAll();
        long totalResponders = allStats.stream()
                .mapToLong(DisasterStatistics::getActiveResponders).sum();
        long totalRequests = allStats.stream()
                .mapToLong(DisasterStatistics::getTotalHelpRequests).sum();
        long resolvedRequests = allStats.stream()
                .mapToLong(DisasterStatistics::getResolvedRequests).sum();

        return new MapSummaryDto(active, monitoring, resolved,
                totalResponders, totalRequests, resolvedRequests);
    }

    public Alert createAlert(AlertRequestDto req) {
        Alert alert = Alert.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .type(req.getType())
                .severity(req.getSeverity())
                .status(req.getStatus() != null ? req.getStatus() : AlertStatus.ACTIVE)
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .radiusKm(req.getRadiusKm())
                .region(req.getRegion())
                .country(req.getCountry())
                .build();
        Alert saved = alertRepository.save(alert);
        notificationService.notifyAlertCreated(saved);
        return saved;
    }

    public Optional<Alert> updateAlert(Integer alertId, AlertRequestDto req) {
        return alertRepository.findById(alertId).map(alert -> {
            alert.setTitle(req.getTitle());
            alert.setDescription(req.getDescription());
            alert.setType(req.getType());
            alert.setSeverity(req.getSeverity());
            if (req.getStatus() != null) alert.setStatus(req.getStatus());
            alert.setLatitude(req.getLatitude());
            alert.setLongitude(req.getLongitude());
            alert.setRadiusKm(req.getRadiusKm());
            alert.setRegion(req.getRegion());
            alert.setCountry(req.getCountry());
            Alert saved = alertRepository.save(alert);
            notificationService.notifyAlertUpdated(saved);
            return saved;
        });
    }

    public boolean deleteAlert(Integer alertId) {
        if (!alertRepository.existsById(alertId)) return false;
        alertRepository.deleteById(alertId);
        notificationService.notifyAlertDeleted(alertId);
        return true;
    }
}
