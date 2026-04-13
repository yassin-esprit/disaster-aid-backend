package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.entities.SensorData;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {


    List<SensorData> findBySensor(Sensor sensor);
    List<SensorData> findBySensorAndRecordedAtBetween(Sensor sensor, LocalDateTime start, LocalDateTime end);
    List<SensorData> findByIsAnomalyTrue();
    List<SensorData> findByRecordedAtAfter(LocalDateTime date);



    Page<SensorData> findBySensor(Sensor sensor, Pageable pageable);
    Page<SensorData> findByIsAnomalyTrue(Pageable pageable);
}
