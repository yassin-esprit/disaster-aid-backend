package tn.esprit.disasteraidbackend.modules.evacuation.dto;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute.RouteStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvacuationRouteDTO {
    private Integer id;
    private Integer alertId;
    private Integer toShelterId;
    private String path;
    private Integer estimatedDurationMin;
    private Integer capacity;
    private RouteStatus status;
    private LocalDateTime activatedAt;
    private LocalDateTime closedAt;
}