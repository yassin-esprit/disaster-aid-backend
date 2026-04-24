package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GeofenceZoneRepository extends JpaRepository<GeofenceZone, Integer> {
    List<GeofenceZone> findByAlert(Alert alert);
    List<GeofenceZone> findByZoneType(GeofenceZone.ZoneType zoneType);
}
