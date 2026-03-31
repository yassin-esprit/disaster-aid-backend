package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import tn.esprit.disasteraidbackend.modules.sensor.services.GeofenceZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geofence-zones")
@RequiredArgsConstructor
public class GeofenceZoneController {

    private final GeofenceZoneService geofenceZoneService;

    @PostMapping
    public ResponseEntity<GeofenceZone> createZone(@RequestBody GeofenceZone zone) {
        GeofenceZone created = geofenceZoneService.createZone(zone);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeofenceZone> getZoneById(@PathVariable Integer id) {
        return geofenceZoneService.getZoneById(id)
                .map(zone -> new ResponseEntity<>(zone, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GeofenceZone>> getAllZones() {
        return new ResponseEntity<>(geofenceZoneService.getAllZones(), HttpStatus.OK);
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<GeofenceZone>> getByAlert(@PathVariable Integer alertId) {
        // We need a method in service that accepts alertId or Alert entity
        // We'll assume we have a method that takes Alert
        Alert alert = new Alert();
        alert.setId(alertId);
        return new ResponseEntity<>(geofenceZoneService.getZonesByAlert(alert), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<GeofenceZone>> getByType(@PathVariable GeofenceZone.ZoneType type) {
        return new ResponseEntity<>(geofenceZoneService.getZonesByType(type), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeofenceZone> updateZone(@PathVariable Integer id, @RequestBody GeofenceZone zone) {
        try {
            GeofenceZone updated = geofenceZoneService.updateZone(id, zone);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Integer id) {
        try {
            geofenceZoneService.deleteZone(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}