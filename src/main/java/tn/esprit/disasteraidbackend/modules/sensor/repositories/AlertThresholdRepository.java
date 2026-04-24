package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import tn.esprit.disasteraidbackend.modules.sensor.entities.AlertThreshold;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AlertThresholdRepository extends JpaRepository<AlertThreshold, Integer> {
    Optional<AlertThreshold> findBySensorTypeAndParameter(Sensor.SensorType sensorType, String parameter);
}
