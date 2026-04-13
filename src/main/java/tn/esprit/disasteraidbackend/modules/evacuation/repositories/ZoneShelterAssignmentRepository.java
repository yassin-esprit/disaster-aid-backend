package tn.esprit.disasteraidbackend.modules.evacuation.repositories;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.ZoneShelterAssignment;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZoneShelterAssignmentRepository extends JpaRepository<ZoneShelterAssignment, Integer> {
    List<ZoneShelterAssignment> findByGeofenceZone(GeofenceZone geofenceZone);
    List<ZoneShelterAssignment> findByShelterId(Integer shelterId);
}
