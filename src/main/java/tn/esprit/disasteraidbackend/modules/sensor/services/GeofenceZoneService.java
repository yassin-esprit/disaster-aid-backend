package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.modules.sensor.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.GeofenceZoneRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GeofenceZoneService {

    private final GeofenceZoneRepository geofenceZoneRepository;
    private final AlertRepository alertRepository;

    public GeofenceZone createZone(GeofenceZone zone) {
        // Ensure alert exists
        Alert alert = alertRepository.findById(zone.getAlert().getId())
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        zone.setAlert(alert);
        zone.setCreatedAt(LocalDateTime.now());
        // If expiresAt not set, default to alert resolution time?
        if (zone.getExpiresAt() == null) {
            zone.setExpiresAt(LocalDateTime.now().plusDays(7)); // default
        }
        return geofenceZoneRepository.save(zone);
    }

    public Optional<GeofenceZone> getZoneById(Integer id) {
        return geofenceZoneRepository.findById(id);
    }

    public List<GeofenceZone> getZonesByAlert(Alert alert) {
        return geofenceZoneRepository.findByAlert(alert);
    }

    public List<GeofenceZone> getZonesByType(GeofenceZone.ZoneType zoneType) {
        return geofenceZoneRepository.findByZoneType(zoneType);
    }

    public GeofenceZone updateZone(Integer id, GeofenceZone updated) {
        return geofenceZoneRepository.findById(id).map(zone -> {
            zone.setPolygon(updated.getPolygon());
            zone.setZoneType(updated.getZoneType());
            zone.setRadius(updated.getRadius());
            zone.setExpiresAt(updated.getExpiresAt());
            return geofenceZoneRepository.save(zone);
        }).orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    public void deleteZone(Integer id) {
        geofenceZoneRepository.deleteById(id);
    }

    public List<GeofenceZone> getAllZones() {
        return geofenceZoneRepository.findAll();
    }

}