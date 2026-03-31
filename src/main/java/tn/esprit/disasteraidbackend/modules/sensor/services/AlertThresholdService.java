package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.AlertThresholdDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.AlertThreshold;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertThresholdService {

    private final AlertThresholdRepository alertThresholdRepository;

    public AlertThresholdDTO createThreshold(AlertThresholdDTO thresholdDTO) {
        AlertThreshold threshold = DTOMapper.toEntity(thresholdDTO);
        return DTOMapper.toDTO(alertThresholdRepository.save(threshold));
    }

    public Optional<AlertThresholdDTO> getThresholdById(Integer id) {
        return alertThresholdRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public Optional<AlertThresholdDTO> getThresholdBySensorTypeAndParameter(Sensor.SensorType sensorType, String parameter) {
        return alertThresholdRepository.findBySensorTypeAndParameter(sensorType, parameter)
                .map(DTOMapper::toDTO);
    }

    public List<AlertThresholdDTO> getAllThresholds() {
        return alertThresholdRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AlertThresholdDTO updateThreshold(Integer id, AlertThresholdDTO thresholdDTO) {
        return alertThresholdRepository.findById(id).map(threshold -> {
            threshold.setSensorType(thresholdDTO.getSensorType());
            threshold.setParameter(thresholdDTO.getParameter());
            threshold.setWarnLevel(thresholdDTO.getWarnLevel());
            threshold.setCriticalLevel(thresholdDTO.getCriticalLevel());
            threshold.setUnit(thresholdDTO.getUnit());
            threshold.setEvaluationWindowSec(thresholdDTO.getEvaluationWindowSec());
            AlertThreshold updated = alertThresholdRepository.save(threshold);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Threshold not found"));
    }

    public void deleteThreshold(Integer id) {
        alertThresholdRepository.deleteById(id);
    }
}