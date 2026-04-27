package tn.esprit.disasteraidbackend.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.GeofenceZoneRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorAlertRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DisasterAidScheduler {

    private final SensorRepository       sensorRepository;
    private final SensorAlertRepository alertRepository;
    private final GeofenceZoneRepository geofenceZoneRepository;
    private final SimpMessagingTemplate  messagingTemplate;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void detectOfflineSensors() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<Sensor> sensors = sensorRepository.findAll();
        int count = 0;
        for (Sensor s : sensors) {
            if (s.getStatus() == Sensor.SensorStatus.ACTIVE
                    && s.getLastPingAt() != null
                    && s.getLastPingAt().isBefore(threshold)) {
                s.setStatus(Sensor.SensorStatus.OFFLINE);
                sensorRepository.save(s);
                count++;
                log.warn("[Scheduler] Sensor {} marked OFFLINE (last ping: {})", s.getId(), s.getLastPingAt());
            }
        }
        if (count > 0) {
            messagingTemplate.convertAndSend("/topic/sensors",
                    Map.of("event", "SENSORS_OFFLINE", "count", count, "timestamp", LocalDateTime.now().toString()));
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void expireGeofenceZones() {
        LocalDateTime now = LocalDateTime.now();
        List<GeofenceZone> zones = geofenceZoneRepository.findAll();
        int count = 0;
        for (GeofenceZone z : zones) {
            if (z.getExpiresAt() != null && z.getExpiresAt().isBefore(now)) {
                count++;
                log.info("[Scheduler] GeofenceZone {} expired at {}", z.getId(), z.getExpiresAt());
            }
        }
        if (count > 0) {
            messagingTemplate.convertAndSend("/topic/geofence",
                    Map.of("event", "ZONES_EXPIRED", "count", count, "timestamp", now.toString()));
        }
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void autoResolveStaleAlerts() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(48);
        List<Alert> stale = alertRepository.findByStatus(Alert.AlertStatus.MONITORING).stream()
                .filter(a -> a.getIssuedAt() != null && a.getIssuedAt().isBefore(cutoff))
                .toList();
        for (Alert a : stale) {
            a.setStatus(Alert.AlertStatus.RESOLVED);
            a.setResolvedAt(LocalDateTime.now());
            alertRepository.save(a);
            log.info("[Scheduler] Alert {} auto-resolved after 48h in MONITORING", a.getId());
        }
        if (!stale.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/alerts",
                    Map.of("event", "ALERTS_AUTO_RESOLVED", "count", stale.size(),
                            "timestamp", LocalDateTime.now().toString()));
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void dailyAlertSummary() {
        long active         = alertRepository.findByStatus(Alert.AlertStatus.ACTIVE).size();
        long monitoring     = alertRepository.findByStatus(Alert.AlertStatus.MONITORING).size();
        long totalToday     = alertRepository.findByIssuedAtAfter(LocalDateTime.now().minusDays(1)).size();
        long offlineSensors = sensorRepository.findByStatus(Sensor.SensorStatus.OFFLINE).size();
        log.info("[Daily Summary] Active: {}, Monitoring: {}, New (24h): {}, Offline sensors: {}",
                active, monitoring, totalToday, offlineSensors);
        messagingTemplate.convertAndSend("/topic/summary", Map.of(
                "event",          "DAILY_SUMMARY",
                "activeAlerts",   active,
                "monitoring",     monitoring,
                "newToday",       totalToday,
                "offlineSensors", offlineSensors,
                "generatedAt",    LocalDateTime.now().toString()
        ));
    }
}
