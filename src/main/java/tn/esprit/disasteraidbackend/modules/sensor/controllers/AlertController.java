package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.AlertDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.sensor.services.AlertService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertDTO> createAlert(@RequestBody AlertDTO alertDTO) {
        AlertDTO created = alertService.createAlert(alertDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(@PathVariable Integer id) {
        return alertService.getAlertById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<AlertDTO>> getAllAlerts() {
        return new ResponseEntity<>(alertService.getAllAlerts(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AlertDTO>> getByStatus(@PathVariable Alert.AlertStatus status) {
        return new ResponseEntity<>(alertService.getAlertsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AlertDTO>> getByType(@PathVariable Alert.DisasterType type) {
        return new ResponseEntity<>(alertService.getAlertsByDisasterType(type), HttpStatus.OK);
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<AlertDTO>> getBySeverity(@PathVariable Alert.Severity severity) {
        return new ResponseEntity<>(alertService.getAlertsBySeverity(severity), HttpStatus.OK);
    }

    @GetMapping("/since")
    public ResponseEntity<List<AlertDTO>> getAfter(@RequestParam LocalDateTime date) {
        return new ResponseEntity<>(alertService.getAlertsIssuedAfter(date), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable Integer id, @RequestBody AlertDTO alertDTO) {
        try {
            AlertDTO updated = alertService.updateAlert(id, alertDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<AlertDTO> resolveAlert(@PathVariable Integer id) {
        try {
            AlertDTO resolved = alertService.resolveAlert(id);
            return new ResponseEntity<>(resolved, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Integer id) {
        try {
            alertService.deleteAlert(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}