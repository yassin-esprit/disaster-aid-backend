package tn.esprit.disasteraidbackend.modules.incident.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class IncidentStatusHistoryDto {
    private Integer id;
    private String fromStatus;
    private String toStatus;
    private Integer changedBy;
    private String note;
    private LocalDateTime changedAt;
}