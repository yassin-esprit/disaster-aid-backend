package tn.esprit.disasteraidbackend.modules.evacuation.repositories;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteCheckpointRepository extends JpaRepository<RouteCheckpoint, Integer> {
    List<RouteCheckpoint> findByRoute(EvacuationRoute route);
    List<RouteCheckpoint> findByType(RouteCheckpoint.CheckpointType type);
}