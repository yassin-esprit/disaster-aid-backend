package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.modules.sensor.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;

    public Alert createAlert(Alert alert) {
        // Validation
        if (alert.getDisasterType() == null || alert.getSeverity() == null) {
            throw new IllegalArgumentException("Disaster type and severity are required");
        }
        alert.setIssuedAt(LocalDateTime.now());
        alert.setStatus(Alert.AlertStatus.ACTIVE);
        return alertRepository.save(alert);
    }

    // Convenience method to create alert from sensor reading
    public Alert createAlertFromSensor(Sensor sensor, Alert.Severity severity, SensorData reading) {
        Alert alert = new Alert();
        // Map sensor type to disaster type (simplified)
        switch (sensor.getType()) {
            case SEISMIC -> alert.setDisasterType(Alert.DisasterType.EARTHQUAKE);
            case FLOOD_GAUGE -> alert.setDisasterType(Alert.DisasterType.FLOOD);
            case FIRE_SMOKE -> alert.setDisasterType(Alert.DisasterType.WILDFIRE);
            case WEATHER -> alert.setDisasterType(Alert.DisasterType.STORM);
            default -> alert.setDisasterType(Alert.DisasterType.CHEMICAL);
        }
        alert.setSeverity(severity);
        alert.setTriggerSensor(sensor);
        alert.setTitle("Alert triggered by " + sensor.getName());
        alert.setDescription("Sensor reading: " + reading.getValue() + " " + reading.getUnit());
        // Set affected area (can be computed later)
        alert.setAffectedArea(null); // to be set by geofencing logic
        return createAlert(alert);
    }

    public Optional<Alert> getAlertById(Integer id) {
        return alertRepository.findById(id);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<Alert> getAlertsByStatus(Alert.AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    public List<Alert> getAlertsByDisasterType(Alert.DisasterType type) {
        return alertRepository.findByDisasterType(type);
    }

    public List<Alert> getAlertsBySeverity(Alert.Severity severity) {
        return alertRepository.findBySeverity(severity);
    }

    public List<Alert> getAlertsIssuedAfter(LocalDateTime date) {
        return alertRepository.findByIssuedAtAfter(date);
    }

    public Alert updateAlert(Integer id, Alert updated) {
        return alertRepository.findById(id).map(alert -> {
            alert.setDisasterType(updated.getDisasterType());
            alert.setSeverity(updated.getSeverity());
            alert.setAffectedArea(updated.getAffectedArea());
            alert.setPredictedArea2h(updated.getPredictedArea2h());
            alert.setPredictedArea6h(updated.getPredictedArea6h());
            alert.setConfidenceScore(updated.getConfidenceScore());
            alert.setTitle(updated.getTitle());
            alert.setDescription(updated.getDescription());
            return alertRepository.save(alert);
        }).orElseThrow(() -> new RuntimeException("Alert not found"));
    }

    public Alert resolveAlert(Integer id) {
        return alertRepository.findById(id).map(alert -> {
            alert.setStatus(Alert.AlertStatus.RESOLVED);
            alert.setResolvedAt(LocalDateTime.now());
            return alertRepository.save(alert);
        }).orElseThrow(() -> new RuntimeException("Alert not found"));
    }
}