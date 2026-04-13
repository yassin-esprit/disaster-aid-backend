package tn.esprit.disasteraidbackend.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository repo;

    /** Alert count per disaster type in an optional date range. */
    public Map<String, Long> alertCountByType(LocalDateTime from, LocalDateTime to) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countByDisasterType(from, to)) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    /** Alert count per status. */
    public Map<String, Long> alertCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    /** Alert count per severity. */
    public Map<String, Long> alertCountBySeverity() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countBySeverity()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    /** Average sensor reading per sensor over the last N hours. */
    public Map<String, Double> avgReadingPerSensor(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : repo.avgReadingPerSensorSince(since)) {
            result.put("sensor_" + row[0], row[1] == null ? 0.0 : ((Number) row[1]).doubleValue());
        }
        return result;
    }

    /** Percentage of anomalous readings per sensor. */
    public Map<String, Double> anomalyRatePerSensor() {
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : repo.anomalyRatePerSensor()) {
            result.put("sensor_" + row[0], row[1] == null ? 0.0 : ((Number) row[1]).doubleValue());
        }
        return result;
    }

    /** Evacuation route count by status (PLANNED / ACTIVE / BLOCKED / CLOSED). */
    public Map<String, Long> routeCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.routeCountByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    /** Sensor count by status (ACTIVE / OFFLINE / FAULTY / MAINTENANCE). */
    public Map<String, Long> sensorCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.sensorCountByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    /**
     * Full dashboard summary — one call for the frontend to populate all stat cards.
     */
    public Map<String, Object> dashboardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("alertsByType",     alertCountByType(null, null));
        summary.put("alertsByStatus",   alertCountByStatus());
        summary.put("alertsBySeverity", alertCountBySeverity());
        summary.put("routesByStatus",   routeCountByStatus());
        summary.put("sensorsByStatus",  sensorCountByStatus());
        summary.put("newAlertsToday",   repo.countAlertsAfter(LocalDateTime.now().minusHours(24)));
        summary.put("generatedAt",      LocalDateTime.now().toString());
        return summary;
    }
}