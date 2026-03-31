package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDTO;
import tn.esprit.disasteraidbackend.modules.sensor.services.SensorService;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<SensorDTO> createSensor(@RequestBody SensorDTO sensorDTO) {
        SensorDTO created = sensorService.createSensor(sensorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable Integer id) {
        return sensorService.getSensorById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        return new ResponseEntity<>(sensorService.getAllSensors(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SensorDTO>> getByStatus(@PathVariable Sensor.SensorStatus status) {
        return new ResponseEntity<>(sensorService.getSensorsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SensorDTO>> getByType(@PathVariable Sensor.SensorType type) {
        return new ResponseEntity<>(sensorService.getSensorsByType(type), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<SensorDTO>> getByOwner(@PathVariable Integer ownerId) {
        return new ResponseEntity<>(sensorService.getSensorsByOwner(ownerId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> updateSensor(@PathVariable Integer id, @RequestBody SensorDTO sensorDTO) {
        try {
            SensorDTO updated = sensorService.updateSensor(id, sensorDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Integer id) {
        try {
            sensorService.deleteSensor(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/ping")
    public ResponseEntity<SensorDTO> updateLastPing(@PathVariable Integer id) {
        try {
            SensorDTO updated = sensorService.updateLastPing(id);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}