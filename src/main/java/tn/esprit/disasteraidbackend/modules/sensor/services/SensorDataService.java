package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.modules.sensor.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorDataRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;
    private final AlertThresholdRepository alertThresholdRepository;
    private final AlertService alertService; // We'll create AlertService next

    public SensorData recordReading(SensorData reading) {
        // Business logic: validate sensor exists
        Sensor sensor = sensorRepository.findById(reading.getSensor().getId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        reading.setSensor(sensor);
        reading.setRecordedAt(LocalDateTime.now());

        // Check for anomalies (simple example: if value > 1000)
        if (reading.getValue() > 1000) {
            reading.setIsAnomaly(true);
        }

        SensorData saved = sensorDataRepository.save(reading);

        // Evaluate thresholds and possibly trigger alerts
        evaluateAndTriggerAlert(saved);

        return saved;
    }

    private void evaluateAndTriggerAlert(SensorData reading) {
        Sensor sensor = reading.getSensor();
        Optional<AlertThreshold> thresholdOpt = alertThresholdRepository
                .findBySensorTypeAndParameter(sensor.getType(), reading.getUnit());

        if (thresholdOpt.isPresent()) {
            AlertThreshold threshold = thresholdOpt.get();
            Double value = reading.getValue();
            if (value >= threshold.getCriticalLevel()) {
                // Trigger critical alert
                alertService.createAlertFromSensor(sensor, Alert.Severity.CRITICAL, reading);
            } else if (value >= threshold.getWarnLevel()) {
                // Trigger warning alert
                alertService.createAlertFromSensor(sensor, Alert.Severity.HIGH, reading);
            }
        }
    }

    public List<SensorData> getReadingsBySensor(Sensor sensor) {
        return sensorDataRepository.findBySensor(sensor);
    }

    public List<SensorData> getReadingsBySensorBetweenDates(Sensor sensor, LocalDateTime start, LocalDateTime end) {
        return sensorDataRepository.findBySensorAndRecordedAtBetween(sensor, start, end);
    }

    public List<SensorData> getAnomalies() {
        return sensorDataRepository.findByIsAnomalyTrue();
    }

    public List<SensorData> getReadingsAfter(LocalDateTime date) {
        return sensorDataRepository.findByRecordedAtAfter(date);
    }

    public List<SensorData> getReadingsBySensorId(Integer sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        return sensorDataRepository.findBySensor(sensor);
    }

    public List<SensorData> getReadingsBySensorIdBetweenDates(Integer sensorId, LocalDateTime start, LocalDateTime end) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        return sensorDataRepository.findBySensorAndRecordedAtBetween(sensor, start, end);
    }
}