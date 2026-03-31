package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.RouteCheckpointRepository;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.EvacuationRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteCheckpointService {

    private final RouteCheckpointRepository checkpointRepository;
    private final EvacuationRouteRepository evacuationRouteRepository;

    public RouteCheckpoint createCheckpoint(RouteCheckpoint checkpoint) {
        EvacuationRoute route = evacuationRouteRepository.findById(checkpoint.getRoute().getId())
                .orElseThrow(() -> new RuntimeException("Route not found"));
        checkpoint.setRoute(route);
        return checkpointRepository.save(checkpoint);
    }

    public Optional<RouteCheckpoint> getCheckpointById(Integer id) {
        return checkpointRepository.findById(id);
    }

    public List<RouteCheckpoint> getCheckpointsByRoute(EvacuationRoute route) {
        return checkpointRepository.findByRoute(route);
    }

    public List<RouteCheckpoint> getCheckpointsByType(RouteCheckpoint.CheckpointType type) {
        return checkpointRepository.findByType(type);
    }

    public RouteCheckpoint updateCheckpoint(Integer id, RouteCheckpoint updated) {
        return checkpointRepository.findById(id).map(cp -> {
            cp.setLocation(updated.getLocation());
            cp.setLabel(updated.getLabel());
            cp.setType(updated.getType());
            cp.setResponderIds(updated.getResponderIds());
            cp.setIsActive(updated.getIsActive());
            return checkpointRepository.save(cp);
        }).orElseThrow(() -> new RuntimeException("Checkpoint not found"));
    }

    public void deleteCheckpoint(Integer id) {
        checkpointRepository.deleteById(id);
    }
    public List<RouteCheckpoint> getAllCheckpoints() {
        return checkpointRepository.findAll();
    }
}