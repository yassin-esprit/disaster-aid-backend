package tn.esprit.disasteraidbackend.modules.evacuation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ZoneShelterAssignmentDTO {
    private Integer id;
    private Integer geofenceZoneId;
    private Integer shelterId;
    private Integer priority;
    private Integer estimatedUsersCount;
    private LocalDateTime createdAt;
}
