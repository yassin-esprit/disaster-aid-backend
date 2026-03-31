package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import tn.esprit.disasteraidbackend.modules.sensor.entities.SensorData;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
    List<SensorData> findBySensor(Sensor sensor);
    List<SensorData> findBySensorAndRecordedAtBetween(Sensor sensor, LocalDateTime start, LocalDateTime end);
    List<SensorData> findByIsAnomalyTrue();
    List<SensorData> findByRecordedAtAfter(LocalDateTime recordedAt);
}