package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.modules.sensor.entities.AlertThreshold;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertThresholdService {

    private final AlertThresholdRepository alertThresholdRepository;

    public AlertThreshold createThreshold(AlertThreshold threshold) {
        if (threshold.getSensorType() == null || threshold.getParameter() == null) {
            throw new IllegalArgumentException("Sensor type and parameter are required");
        }
        return alertThresholdRepository.save(threshold);
    }

    public Optional<AlertThreshold> getThresholdById(Integer id) {
        return alertThresholdRepository.findById(id);
    }

    public Optional<AlertThreshold> getThresholdBySensorTypeAndParameter(Sensor.SensorType sensorType, String parameter) {
        return alertThresholdRepository.findBySensorTypeAndParameter(sensorType, parameter);
    }

    public List<AlertThreshold> getAllThresholds() {
        return alertThresholdRepository.findAll();
    }

    public AlertThreshold updateThreshold(Integer id, AlertThreshold updated) {
        return alertThresholdRepository.findById(id).map(threshold -> {
            threshold.setSensorType(updated.getSensorType());
            threshold.setParameter(updated.getParameter());
            threshold.setWarnLevel(updated.getWarnLevel());
            threshold.setCriticalLevel(updated.getCriticalLevel());
            threshold.setUnit(updated.getUnit());
            threshold.setEvaluationWindowSec(updated.getEvaluationWindowSec());
            return alertThresholdRepository.save(threshold);
        }).orElseThrow(() -> new RuntimeException("Threshold not found"));
    }

    public void deleteThreshold(Integer id) {
        alertThresholdRepository.deleteById(id);
    }
}