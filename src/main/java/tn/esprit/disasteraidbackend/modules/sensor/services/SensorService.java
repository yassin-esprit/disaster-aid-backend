package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorService {

    private final SensorRepository sensorRepository;

    public Sensor createSensor(Sensor sensor) {
        // Business logic: ensure location is provided, set default status, etc.
        if (sensor.getLocation() == null) {
            throw new IllegalArgumentException("Sensor location is required");
        }
        if (sensor.getType() == null) {
            throw new IllegalArgumentException("Sensor type is required");
        }
        sensor.setStatus(Sensor.SensorStatus.ACTIVE);
        return sensorRepository.save(sensor);
    }

    public Optional<Sensor> getSensorById(Integer id) {
        return sensorRepository.findById(id);
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<Sensor> getSensorsByStatus(Sensor.SensorStatus status) {
        return sensorRepository.findByStatus(status);
    }

    public List<Sensor> getSensorsByType(Sensor.SensorType type) {
        return sensorRepository.findByType(type);
    }

    public List<Sensor> getSensorsByOwner(Integer ownerId) {
        return sensorRepository.findByOwnerId(ownerId);
    }

    public Sensor updateSensor(Integer id, Sensor updated) {
        return sensorRepository.findById(id).map(sensor -> {
            // Update allowed fields
            sensor.setName(updated.getName());
            sensor.setType(updated.getType());
            sensor.setLocation(updated.getLocation());
            sensor.setOwnerId(updated.getOwnerId());
            sensor.setManufacturer(updated.getManufacturer());
            sensor.setStatus(updated.getStatus());
            sensor.setLastPingAt(updated.getLastPingAt());
            sensor.setCalibrationDate(updated.getCalibrationDate());
            return sensorRepository.save(sensor);
        }).orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
    }

    public void deleteSensor(Integer id) {
        if (!sensorRepository.existsById(id)) {
            throw new RuntimeException("Sensor not found with id: " + id);
        }
        sensorRepository.deleteById(id);
    }

    public Sensor updateLastPing(Integer id) {
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setLastPingAt(java.time.LocalDateTime.now());
            return sensorRepository.save(sensor);
        }).orElseThrow(() -> new RuntimeException("Sensor not found"));
    }
}