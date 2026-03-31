package tn.esprit.disasteraidbackend.modules.evacuation.repositories;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvacuationRouteRepository extends JpaRepository<EvacuationRoute, Integer> {
    List<EvacuationRoute> findByAlert(Alert alert);
    List<EvacuationRoute> findByStatus(EvacuationRoute.RouteStatus status);
    List<EvacuationRoute> findByToShelterId(Integer shelterId);
}