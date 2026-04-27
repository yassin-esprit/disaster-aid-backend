package tn.esprit.disasteraidbackend.modules.sensor.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.GeofenceZoneDTO;
import tn.esprit.disasteraidbackend.modules.sensor.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.GeofenceZoneRepository;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorAlertRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GeofenceZoneService {

    private final GeofenceZoneRepository geofenceZoneRepository;
    private final SensorAlertRepository alertRepository;
    private final GeometryFactory geometryFactory;

    public GeofenceZoneDTO createZone(GeofenceZoneDTO zoneDTO) {
        GeofenceZone zone = DTOMapper.toEntity(zoneDTO, geometryFactory);
        Alert alert = alertRepository.findById(zoneDTO.getAlertId())
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        zone.setAlert(alert);
        zone.setCreatedAt(LocalDateTime.now());
        if (zone.getExpiresAt() == null) {
            zone.setExpiresAt(LocalDateTime.now().plusDays(7));
        }
        GeofenceZone saved = geofenceZoneRepository.save(zone);
        return DTOMapper.toDTO(saved);
    }

    public Optional<GeofenceZoneDTO> getZoneById(Integer id) {
        return geofenceZoneRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<GeofenceZoneDTO> getZonesByAlertId(Integer alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        return geofenceZoneRepository.findByAlert(alert).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<GeofenceZoneDTO> getZonesByType(GeofenceZone.ZoneType zoneType) {
        return geofenceZoneRepository.findByZoneType(zoneType).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GeofenceZoneDTO updateZone(Integer id, GeofenceZoneDTO zoneDTO) {
        return geofenceZoneRepository.findById(id).map(zone -> {
            zone.setPolygon((org.locationtech.jts.geom.Polygon) DTOMapper.fromWKT(zoneDTO.getPolygon(), geometryFactory));
            zone.setZoneType(zoneDTO.getZoneType());
            zone.setRadius(zoneDTO.getRadius());
            zone.setExpiresAt(zoneDTO.getExpiresAt());
            GeofenceZone updated = geofenceZoneRepository.save(zone);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    public void deleteZone(Integer id) {
        geofenceZoneRepository.deleteById(id);
    }

    public List<GeofenceZoneDTO> getAllZones() {
        return geofenceZoneRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
