package tn.esprit.disasteraidbackend.modules.sensor.dto;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor.SensorType;
import lombok.Data;

@Data
public class AlertThresholdDTO {
    private Integer id;
    private SensorType sensorType;
    private String parameter;
    private Double warnLevel;
    private Double criticalLevel;
    private String unit;
    private Integer evaluationWindowSec;
}
