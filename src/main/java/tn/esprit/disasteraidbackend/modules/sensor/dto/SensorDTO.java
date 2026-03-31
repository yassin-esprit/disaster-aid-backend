package tn.esprit.disasteraidbackend.modules.sensor.dto;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor.SensorType;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor.SensorStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class SensorDTO {
    private Integer id;          // changed from Long
    private String name;
    private SensorType type;
    private String location;
    private Integer ownerId;
    private String manufacturer;
    private SensorStatus status;
    private LocalDateTime lastPingAt;
    private LocalDate calibrationDate;
}