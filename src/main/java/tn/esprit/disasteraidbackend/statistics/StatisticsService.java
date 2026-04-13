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

    public Map<String, Long> alertCountByType(LocalDateTime from, LocalDateTime to) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countByDisasterType(from, to)) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    public Map<String, Long> alertCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    public Map<String, Long> alertCountBySeverity() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.countBySeverity()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    public Map<String, Double> avgReadingPerSensor(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : repo.avgReadingPerSensorSince(since)) {
            result.put("sensor_" + row[0], row[1] == null ? 0.0 : ((Number) row[1]).doubleValue());
        }
        return result;
    }

    public Map<String, Double> anomalyRatePerSensor() {
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : repo.anomalyRatePerSensor()) {
            result.put("sensor_" + row[0], row[1] == null ? 0.0 : ((Number) row[1]).doubleValue());
        }
        return result;
    }

    public Map<String, Long> routeCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.routeCountByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    public Map<String, Long> sensorCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : repo.sensorCountByStatus()) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

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
