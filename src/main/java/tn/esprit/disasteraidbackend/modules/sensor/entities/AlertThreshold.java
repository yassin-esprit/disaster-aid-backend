package tn.esprit.disasteraidbackend.modules.sensor.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alert_thresholds")
@Getter
@Setter
public class AlertThreshold extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Sensor.SensorType sensorType;

    private String parameter;

    private Double warnLevel;

    private Double criticalLevel;

    private String unit;

    private Integer evaluationWindowSec;
}