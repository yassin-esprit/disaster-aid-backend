package tn.esprit.disasteraidbackend.modules.sensor.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SensorDataDTO {
    private Integer id;          // changed from Long
    private Integer sensorId;     // changed from Long
    private Double value;
    private String unit;
    private LocalDateTime recordedAt;
    private Object rawPayload;
    private Boolean isAnomaly;
}