package tn.esprit.disasteraidbackend.modules.evacuation.dto;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute.RouteStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvacuationRouteDTO {
    private Integer id;

    private Integer alertId;
    private String alertTitle;

    private Integer toShelterId;
    private String shelterName;

    private Double fromLatitude;
    private Double fromLongitude;
    private Double toLatitude;
    private Double toLongitude;

    private String path;
    private Double distanceKm;
    private Integer estimatedDurationMin;
    private Integer capacity;
    private RouteStatus status;
    private LocalDateTime activatedAt;
    private LocalDateTime closedAt;
}
