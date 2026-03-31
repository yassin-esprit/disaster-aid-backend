package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.GeofenceZoneDTO;
import tn.esprit.disasteraidbackend.modules.sensor.services.GeofenceZoneService;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
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
    public ResponseEntity<GeofenceZoneDTO> createZone(@RequestBody GeofenceZoneDTO zoneDTO) {
        GeofenceZoneDTO created = geofenceZoneService.createZone(zoneDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeofenceZoneDTO> getZoneById(@PathVariable Integer id) {
        return geofenceZoneService.getZoneById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GeofenceZoneDTO>> getAllZones() {
        return new ResponseEntity<>(geofenceZoneService.getAllZones(), HttpStatus.OK);
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<GeofenceZoneDTO>> getByAlert(@PathVariable Integer alertId) {
        return new ResponseEntity<>(geofenceZoneService.getZonesByAlertId(alertId), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<GeofenceZoneDTO>> getByType(@PathVariable GeofenceZone.ZoneType type) {
        return new ResponseEntity<>(geofenceZoneService.getZonesByType(type), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeofenceZoneDTO> updateZone(@PathVariable Integer id, @RequestBody GeofenceZoneDTO zoneDTO) {
        try {
            GeofenceZoneDTO updated = geofenceZoneService.updateZone(id, zoneDTO);
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