package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    List<Sensor> findByStatus(Sensor.SensorStatus status);
    List<Sensor> findByType(Sensor.SensorType type);
    List<Sensor> findByOwnerId(Integer ownerId);
}