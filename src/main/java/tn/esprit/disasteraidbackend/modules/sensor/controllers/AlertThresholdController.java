package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.AlertThresholdDTO;
import tn.esprit.disasteraidbackend.modules.sensor.services.AlertThresholdService;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
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
    public ResponseEntity<AlertThresholdDTO> createThreshold(@RequestBody AlertThresholdDTO thresholdDTO) {
        AlertThresholdDTO created = alertThresholdService.createThreshold(thresholdDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertThresholdDTO> getThresholdById(@PathVariable Integer id) {
        return alertThresholdService.getThresholdById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<AlertThresholdDTO>> getAllThresholds() {
        return new ResponseEntity<>(alertThresholdService.getAllThresholds(), HttpStatus.OK);
    }

    @GetMapping("/sensor-type/{sensorType}/parameter/{parameter}")
    public ResponseEntity<AlertThresholdDTO> getBySensorTypeAndParameter(
            @PathVariable Sensor.SensorType sensorType,
            @PathVariable String parameter) {
        return alertThresholdService.getThresholdBySensorTypeAndParameter(sensorType, parameter)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertThresholdDTO> updateThreshold(@PathVariable Integer id, @RequestBody AlertThresholdDTO thresholdDTO) {
        try {
            AlertThresholdDTO updated = alertThresholdService.updateThreshold(id, thresholdDTO);
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
