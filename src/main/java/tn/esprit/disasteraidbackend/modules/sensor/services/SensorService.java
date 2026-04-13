package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorService {

    private final SensorRepository sensorRepository;
    private final GeometryFactory geometryFactory;

    @CacheEvict(value = "sensors", allEntries = true)
    public SensorDTO createSensor(SensorDTO sensorDTO) {
        Sensor sensor = DTOMapper.toEntity(sensorDTO, geometryFactory);
        if (sensor.getLocation() == null) {
            throw new IllegalArgumentException("Sensor location is required");
        }
        if (sensor.getType() == null) {
            throw new IllegalArgumentException("Sensor type is required");
        }
        sensor.setStatus(Sensor.SensorStatus.ACTIVE);
        Sensor saved = sensorRepository.save(sensor);
        return DTOMapper.toDTO(saved);
    }

    @Cacheable(value = "sensors", key = "#id")
    public Optional<SensorDTO> getSensorById(Integer id) {
        return sensorRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    @Cacheable(value = "sensors", key = "'all'")
    public List<SensorDTO> getAllSensors() {
        return sensorRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDTO> getSensorsByStatus(Sensor.SensorStatus status) {
        return sensorRepository.findByStatus(status).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDTO> getSensorsByType(Sensor.SensorType type) {
        return sensorRepository.findByType(type).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SensorDTO> getSensorsByOwner(Integer ownerId) {
        return sensorRepository.findByOwnerId(ownerId).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "sensors", allEntries = true)
    public SensorDTO updateSensor(Integer id, SensorDTO sensorDTO) {
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setName(sensorDTO.getName());
            sensor.setType(sensorDTO.getType());
            sensor.setLocation((org.locationtech.jts.geom.Point) DTOMapper.fromWKT(sensorDTO.getLocation(), geometryFactory));
            sensor.setOwnerId(sensorDTO.getOwnerId());
            sensor.setManufacturer(sensorDTO.getManufacturer());
            sensor.setStatus(sensorDTO.getStatus());
            sensor.setLastPingAt(sensorDTO.getLastPingAt());
            sensor.setCalibrationDate(sensorDTO.getCalibrationDate());
            Sensor updated = sensorRepository.save(sensor);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
    }

    @CacheEvict(value = "sensors", allEntries = true)
    public void deleteSensor(Integer id) {
        if (!sensorRepository.existsById(id)) {
            throw new RuntimeException("Sensor not found with id: " + id);
        }
        sensorRepository.deleteById(id);
    }

    @CacheEvict(value = "sensors", allEntries = true)
    public SensorDTO updateLastPing(Integer id) {
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setLastPingAt(LocalDateTime.now());
            Sensor updated = sensorRepository.save(sensor);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Sensor not found"));
    }
}
