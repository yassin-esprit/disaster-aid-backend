package tn.esprit.disasteraidbackend.modules.sensor.dto;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertDTO {
    private Integer id;                      // changed from Long
    private DisasterType disasterType;
    private Severity severity;
    private AlertStatus status;
    private String affectedArea;
    private String predictedArea2h;
    private String predictedArea6h;
    private Double confidenceScore;
    private Integer triggerSensorId;          // changed from Long
    private Integer issuedBy;
    private LocalDateTime issuedAt;
    private LocalDateTime resolvedAt;
    private String title;
    private String description;
}