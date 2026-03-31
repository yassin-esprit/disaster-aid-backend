package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.evacuation.dto.EvacuationRouteDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.EvacuationRouteRepository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvacuationRouteService {

    private final EvacuationRouteRepository evacuationRouteRepository;
    private final AlertRepository alertRepository;
    private final GeometryFactory geometryFactory;

    public EvacuationRouteDTO createRoute(EvacuationRouteDTO routeDTO) {
        EvacuationRoute route = DTOMapper.toEntity(routeDTO, geometryFactory);
        Alert alert = alertRepository.findById(routeDTO.getAlertId())
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        route.setAlert(alert);
        route.setStatus(EvacuationRoute.RouteStatus.PLANNED);
        EvacuationRoute saved = evacuationRouteRepository.save(route);
        return DTOMapper.toDTO(saved);
    }

    public Optional<EvacuationRouteDTO> getRouteById(Integer id) {
        return evacuationRouteRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<EvacuationRouteDTO> getRoutesByAlertId(Integer alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        return evacuationRouteRepository.findByAlert(alert).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EvacuationRouteDTO> getRoutesByStatus(EvacuationRoute.RouteStatus status) {
        return evacuationRouteRepository.findByStatus(status).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EvacuationRouteDTO> getRoutesByShelter(Integer shelterId) {
        return evacuationRouteRepository.findByToShelterId(shelterId).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EvacuationRouteDTO updateRoute(Integer id, EvacuationRouteDTO routeDTO) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setPath((LineString) DTOMapper.fromWKT(routeDTO.getPath(), geometryFactory));
            route.setEstimatedDurationMin(routeDTO.getEstimatedDurationMin());
            route.setCapacity(routeDTO.getCapacity());
            route.setToShelterId(routeDTO.getToShelterId());
            EvacuationRoute updated = evacuationRouteRepository.save(route);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRouteDTO activateRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.ACTIVE);
            route.setActivatedAt(LocalDateTime.now());
            EvacuationRoute updated = evacuationRouteRepository.save(route);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRouteDTO closeRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.CLOSED);
            route.setClosedAt(LocalDateTime.now());
            EvacuationRoute updated = evacuationRouteRepository.save(route);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public void deleteRoute(Integer id) {
        evacuationRouteRepository.deleteById(id);
    }

    public List<EvacuationRouteDTO> getAllRoutes() {
        return evacuationRouteRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}