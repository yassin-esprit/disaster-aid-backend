package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.AlertDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.sensor.services.AlertService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /** Only admins can create alerts */
    @PostMapping
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'AGENCY_ADMIN')")
    public ResponseEntity<AlertDTO> createAlert(@RequestBody AlertDTO alertDTO) {
        AlertDTO created = alertService.createAlert(alertDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** Any authenticated user can view a single alert */
    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(@PathVariable Integer id) {
        return alertService.getAlertById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /** Any authenticated user can list alerts */
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

    /** Only admins can update alerts */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'AGENCY_ADMIN')")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable Integer id, @RequestBody AlertDTO alertDTO) {
        try {
            AlertDTO updated = alertService.updateAlert(id, alertDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Only admins can resolve alerts */
    @PatchMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'AGENCY_ADMIN')")
    public ResponseEntity<AlertDTO> resolveAlert(@PathVariable Integer id) {
        try {
            AlertDTO resolved = alertService.resolveAlert(id);
            return new ResponseEntity<>(resolved, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Only admins can delete alerts */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'AGENCY_ADMIN')")
    public ResponseEntity<Void> deleteAlert(@PathVariable Integer id) {
        try {
            alertService.deleteAlert(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}