package tn.esprit.disasteraidbackend.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(service.dashboardSummary());
    }

    @GetMapping("/alerts/by-type")
    public ResponseEntity<Map<String, Long>> alertsByType(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.alertCountByType(from, to));
    }

    @GetMapping("/alerts/by-status")
    public ResponseEntity<Map<String, Long>> alertsByStatus() {
        return ResponseEntity.ok(service.alertCountByStatus());
    }

    @GetMapping("/alerts/by-severity")
    public ResponseEntity<Map<String, Long>> alertsBySeverity() {
        return ResponseEntity.ok(service.alertCountBySeverity());
    }

    @GetMapping("/sensors/avg")
    public ResponseEntity<Map<String, Double>> avgReadings(
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(service.avgReadingPerSensor(hours));
    }

    @GetMapping("/sensors/anomaly-rate")
    public ResponseEntity<Map<String, Double>> anomalyRate() {
        return ResponseEntity.ok(service.anomalyRatePerSensor());
    }

    @GetMapping("/routes/by-status")
    public ResponseEntity<Map<String, Long>> routesByStatus() {
        return ResponseEntity.ok(service.routeCountByStatus());
    }

    @GetMapping("/sensors/by-status")
    public ResponseEntity<Map<String, Long>> sensorsByStatus() {
        return ResponseEntity.ok(service.sensorCountByStatus());
    }
}
