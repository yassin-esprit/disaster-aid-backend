package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.entities.AlertThreshold;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.services.AlertThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert-thresholds")
@RequiredArgsConstructor
public class AlertThresholdController {

    private final AlertThresholdService alertThresholdService;

    @PostMapping
    public ResponseEntity<AlertThreshold> createThreshold(@RequestBody AlertThreshold threshold) {
        AlertThreshold created = alertThresholdService.createThreshold(threshold);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertThreshold> getThresholdById(@PathVariable Integer id) {
        return alertThresholdService.getThresholdById(id)
                .map(threshold -> new ResponseEntity<>(threshold, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<AlertThreshold>> getAllThresholds() {
        return new ResponseEntity<>(alertThresholdService.getAllThresholds(), HttpStatus.OK);
    }

    @GetMapping("/sensor-type/{sensorType}/parameter/{parameter}")
    public ResponseEntity<AlertThreshold> getBySensorTypeAndParameter(
            @PathVariable Sensor.SensorType sensorType,
            @PathVariable String parameter) {
        return alertThresholdService.getThresholdBySensorTypeAndParameter(sensorType, parameter)
                .map(threshold -> new ResponseEntity<>(threshold, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertThreshold> updateThreshold(@PathVariable Integer id, @RequestBody AlertThreshold threshold) {
        try {
            AlertThreshold updated = alertThresholdService.updateThreshold(id, threshold);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThreshold(@PathVariable Integer id) {
        try {
            alertThresholdService.deleteThreshold(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}