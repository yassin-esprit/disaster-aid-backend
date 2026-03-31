package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.services.SensorService;
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
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        Sensor created = sensorService.createSensor(sensor);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Integer id) {
        return sensorService.getSensorById(id)
                .map(sensor -> new ResponseEntity<>(sensor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensors() {
        return new ResponseEntity<>(sensorService.getAllSensors(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Sensor>> getByStatus(@PathVariable Sensor.SensorStatus status) {
        return new ResponseEntity<>(sensorService.getSensorsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Sensor>> getByType(@PathVariable Sensor.SensorType type) {
        return new ResponseEntity<>(sensorService.getSensorsByType(type), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Sensor>> getByOwner(@PathVariable Integer ownerId) {
        return new ResponseEntity<>(sensorService.getSensorsByOwner(ownerId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Integer id, @RequestBody Sensor sensor) {
        try {
            Sensor updated = sensorService.updateSensor(id, sensor);
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
    public ResponseEntity<Sensor> updateLastPing(@PathVariable Integer id) {
        try {
            Sensor updated = sensorService.updateLastPing(id);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}