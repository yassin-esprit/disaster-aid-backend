package tn.esprit.disasteraidbackend.modules.sensor.dto;

import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone.ZoneType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GeofenceZoneDTO {
    private Integer id;          // changed from Long
    private Integer alertId;      // changed from Long
    private String polygon;
    private ZoneType zoneType;
    private Double radius;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}