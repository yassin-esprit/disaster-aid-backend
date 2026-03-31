package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.EvacuationRouteRepository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EvacuationRouteService {

    private final EvacuationRouteRepository evacuationRouteRepository;
    private final AlertRepository alertRepository;

    public EvacuationRoute createRoute(EvacuationRoute route) {
        // Validate alert exists
        Alert alert = alertRepository.findById(route.getAlert().getId())
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        route.setAlert(alert);
        if (route.getPath() == null) {
            throw new IllegalArgumentException("Route path is required");
        }
        route.setStatus(EvacuationRoute.RouteStatus.PLANNED);
        return evacuationRouteRepository.save(route);
    }

    public Optional<EvacuationRoute> getRouteById(Integer id) {
        return evacuationRouteRepository.findById(id);
    }

    public List<EvacuationRoute> getRoutesByAlert(Alert alert) {
        return evacuationRouteRepository.findByAlert(alert);
    }

    public List<EvacuationRoute> getRoutesByStatus(EvacuationRoute.RouteStatus status) {
        return evacuationRouteRepository.findByStatus(status);
    }

    public List<EvacuationRoute> getRoutesByShelter(Integer shelterId) {
        return evacuationRouteRepository.findByToShelterId(shelterId);
    }

    public EvacuationRoute updateRoute(Integer id, EvacuationRoute updated) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setPath(updated.getPath());
            route.setEstimatedDurationMin(updated.getEstimatedDurationMin());
            route.setCapacity(updated.getCapacity());
            route.setToShelterId(updated.getToShelterId());
            return evacuationRouteRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRoute activateRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.ACTIVE);
            route.setActivatedAt(LocalDateTime.now());
            return evacuationRouteRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRoute closeRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.CLOSED);
            route.setClosedAt(LocalDateTime.now());
            return evacuationRouteRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public void deleteRoute(Integer id) {
        evacuationRouteRepository.deleteById(id);
    }

    public List<EvacuationRoute> getAllRoutes() {
        return evacuationRouteRepository.findAll();
    }


}