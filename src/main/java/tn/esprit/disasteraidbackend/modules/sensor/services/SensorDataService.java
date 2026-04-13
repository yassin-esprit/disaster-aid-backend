package tn.esprit.disasteraidbackend.modules.sensor.services;

import org.springframework.data.domain.Pageable;
import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDataDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;
    private final AlertThresholdRepository alertThresholdRepository;
    private final AlertService alertService;

    public SensorDataDTO recordReading(SensorDataDTO readingDTO) {

        SensorData reading = DTOMapper.toEntity(readingDTO);

        Sensor sensor = sensorRepository.findById(readingDTO.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        reading.setSensor(sensor);
        reading.setRecordedAt(LocalDateTime.now());


        if (reading.getValue() > 1000) {
            reading.setIsAnomaly(true);
        }

        SensorData saved = sensorDataRepository.save(reading);


        evaluateAndTriggerAlert(saved);

        return DTOMapper.toDTO(saved);
    }

    private void evaluateAndTriggerAlert(SensorData reading) {
        Sensor sensor = reading.getSensor();
        Optional<AlertThreshold> thresholdOpt = alertThresholdRepository
                .findBySensorTypeAndParameter(sensor.getType(), reading.getUnit());

        if (thresholdOpt.isPresent()) {
            AlertThreshold threshold = thresholdOpt.get();
            Double value = reading.getValue();
            if (value >= threshold.getCriticalLevel()) {
                alertService.createAlertFromSensor(sensor, Alert.Severity.CRITICAL, reading);
            } else if (value >= threshold.getWarnLevel()) {
                alertService.createAlertFromSensor(sensor, Alert.Severity.HIGH, reading);
            }
        }
    }

    public List<SensorDataDTO> getReadingsBySensorId(Integer sensorId, Pageable pageable) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        return sensorDataRepository.findBySensor(sensor).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDataDTO> getReadingsBySensorIdBetweenDates(Integer sensorId, LocalDateTime start, LocalDateTime end) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        return sensorDataRepository.findBySensorAndRecordedAtBetween(sensor, start, end).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDataDTO> getAnomalies() {
        return sensorDataRepository.findByIsAnomalyTrue().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDataDTO> getReadingsAfter(LocalDateTime date) {
        return sensorDataRepository.findByRecordedAtAfter(date).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
