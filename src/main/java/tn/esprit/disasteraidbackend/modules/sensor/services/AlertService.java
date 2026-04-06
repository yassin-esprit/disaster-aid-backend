package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.AlertDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;
    private final GeometryFactory geometryFactory;

    public AlertDTO createAlert(AlertDTO alertDTO) {
        Alert alert = DTOMapper.toEntity(alertDTO, geometryFactory);
        if (alertDTO.getTriggerSensorId() != null) {
            Sensor sensor = sensorRepository.findById(alertDTO.getTriggerSensorId())
                    .orElseThrow(() -> new RuntimeException("Sensor not found"));
            alert.setTriggerSensor(sensor);
        }
        alert.setIssuedAt(LocalDateTime.now());
        alert.setStatus(Alert.AlertStatus.ACTIVE);
        Alert saved = alertRepository.save(alert);
        return DTOMapper.toDTO(saved);
    }

    // Internal convenience method (returns entity, used by SensorDataService)
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
        alert.setIssuedAt(LocalDateTime.now());
        alert.setStatus(Alert.AlertStatus.ACTIVE);
        return alertRepository.save(alert);
    }

    public Optional<AlertDTO> getAlertById(Integer id) {
        return alertRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getAlertsByStatus(Alert.AlertStatus status) {
        return alertRepository.findByStatus(status).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getAlertsByDisasterType(Alert.DisasterType type) {
        return alertRepository.findByDisasterType(type).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getAlertsBySeverity(Alert.Severity severity) {
        return alertRepository.findBySeverity(severity).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getAlertsIssuedAfter(LocalDateTime date) {
        return alertRepository.findByIssuedAtAfter(date).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AlertDTO updateAlert(Integer id, AlertDTO alertDTO) {
        return alertRepository.findById(id).map(alert -> {
            alert.setDisasterType(alertDTO.getDisasterType());
            alert.setSeverity(alertDTO.getSeverity());
            alert.setAffectedArea((org.locationtech.jts.geom.Polygon) DTOMapper.fromWKT(alertDTO.getAffectedArea(), geometryFactory));
            alert.setPredictedArea2h((org.locationtech.jts.geom.Polygon) DTOMapper.fromWKT(alertDTO.getPredictedArea2h(), geometryFactory));
            alert.setPredictedArea6h((org.locationtech.jts.geom.Polygon) DTOMapper.fromWKT(alertDTO.getPredictedArea6h(), geometryFactory));
            alert.setConfidenceScore(alertDTO.getConfidenceScore());
            alert.setTitle(alertDTO.getTitle());
            alert.setDescription(alertDTO.getDescription());
            if (alertDTO.getTriggerSensorId() != null) {
                Sensor sensor = sensorRepository.findById(alertDTO.getTriggerSensorId())
                        .orElseThrow(() -> new RuntimeException("Sensor not found"));
                alert.setTriggerSensor(sensor);
            }
            Alert updated = alertRepository.save(alert);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Alert not found"));
    }

    public AlertDTO resolveAlert(Integer id) {
        return alertRepository.findById(id).map(alert -> {
            alert.setStatus(Alert.AlertStatus.RESOLVED);
            alert.setResolvedAt(LocalDateTime.now());
            Alert updated = alertRepository.save(alert);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Alert not found"));
    }
    public void deleteAlert(Integer id) {
        if (!alertRepository.existsById(id)) {
            throw new RuntimeException("Alert not found with id: " + id);
        }
        alertRepository.deleteById(id);
    }
}