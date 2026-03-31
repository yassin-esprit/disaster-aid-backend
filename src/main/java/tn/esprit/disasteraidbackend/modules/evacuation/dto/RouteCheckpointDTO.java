package tn.esprit.disasteraidbackend.modules.evacuation.dto;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint.CheckpointType;
import lombok.Data;
import java.util.List;

@Data
public class RouteCheckpointDTO {
    private Integer id;
    private Integer routeId;
    private String location;
    private String label;
    private CheckpointType type;
    private List<Integer> responderIds;
    private Boolean isActive;
}