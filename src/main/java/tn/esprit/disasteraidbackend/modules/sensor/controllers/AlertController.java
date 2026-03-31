package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        Alert created = alertService.createAlert(alert);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Integer id) {
        return alertService.getAlertById(id)
                .map(alert -> new ResponseEntity<>(alert, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return new ResponseEntity<>(alertService.getAllAlerts(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Alert>> getByStatus(@PathVariable Alert.AlertStatus status) {
        return new ResponseEntity<>(alertService.getAlertsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Alert>> getByType(@PathVariable Alert.DisasterType type) {
        return new ResponseEntity<>(alertService.getAlertsByDisasterType(type), HttpStatus.OK);
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Alert>> getBySeverity(@PathVariable Alert.Severity severity) {
        return new ResponseEntity<>(alertService.getAlertsBySeverity(severity), HttpStatus.OK);
    }

    @GetMapping("/since")
    public ResponseEntity<List<Alert>> getAfter(@RequestParam LocalDateTime date) {
        return new ResponseEntity<>(alertService.getAlertsIssuedAfter(date), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Integer id, @RequestBody Alert alert) {
        try {
            Alert updated = alertService.updateAlert(id, alert);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable Integer id) {
        try {
            Alert resolved = alertService.resolveAlert(id);
            return new ResponseEntity<>(resolved, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}