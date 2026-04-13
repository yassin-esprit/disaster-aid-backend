package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.evacuation.dto.EvacuationRouteDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.EvacuationRouteRepository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
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
    private final OsrmRouteService osrmRouteService;

    public EvacuationRouteDTO createRoute(EvacuationRouteDTO routeDTO) {
        Alert alert = alertRepository.findById(routeDTO.getAlertId())
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + routeDTO.getAlertId()));

        if (routeDTO.getFromLatitude() == null || routeDTO.getFromLongitude() == null
                || routeDTO.getToLatitude() == null || routeDTO.getToLongitude() == null) {
            throw new IllegalArgumentException(
                    "fromLatitude, fromLongitude, toLatitude and toLongitude are required for route computation");
        }

        OsrmRouteService.RouteResult computed = osrmRouteService.computeRoute(
                routeDTO.getFromLatitude(), routeDTO.getFromLongitude(),
                routeDTO.getToLatitude(),   routeDTO.getToLongitude()
        );

        EvacuationRoute route = new EvacuationRoute();
        route.setAlert(alert);
        route.setToShelterId(routeDTO.getToShelterId());
        route.setShelterName(routeDTO.getShelterName());
        route.setFromLatitude(routeDTO.getFromLatitude());
        route.setFromLongitude(routeDTO.getFromLongitude());
        route.setToLatitude(routeDTO.getToLatitude());
        route.setToLongitude(routeDTO.getToLongitude());
        route.setPath(computed.path());
        route.setDistanceKm(computed.distanceKm());
        route.setEstimatedDurationMin(computed.estimatedDurationMin());
        route.setCapacity(routeDTO.getCapacity());
        route.setStatus(EvacuationRoute.RouteStatus.PLANNED);

        EvacuationRoute saved = evacuationRouteRepository.save(route);
        return DTOMapper.toDTO(saved);
    }

    public Optional<EvacuationRouteDTO> getRouteById(Integer id) {
        return evacuationRouteRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<EvacuationRouteDTO> getAllRoutes() {
        return evacuationRouteRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
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

    public EvacuationRouteDTO recomputeRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            if (route.getFromLatitude() == null || route.getToLatitude() == null) {
                throw new IllegalArgumentException("Route has no coordinates stored for recomputation");
            }
            OsrmRouteService.RouteResult computed = osrmRouteService.computeRoute(
                    route.getFromLatitude(), route.getFromLongitude(),
                    route.getToLatitude(),   route.getToLongitude()
            );
            route.setPath(computed.path());
            route.setDistanceKm(computed.distanceKm());
            route.setEstimatedDurationMin(computed.estimatedDurationMin());
            EvacuationRoute updated = evacuationRouteRepository.save(route);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRouteDTO updateRoute(Integer id, EvacuationRouteDTO routeDTO) {
        return evacuationRouteRepository.findById(id).map(route -> {
            if (routeDTO.getFromLatitude() != null && routeDTO.getToLatitude() != null) {
                OsrmRouteService.RouteResult computed = osrmRouteService.computeRoute(
                        routeDTO.getFromLatitude(), routeDTO.getFromLongitude(),
                        routeDTO.getToLatitude(),   routeDTO.getToLongitude()
                );
                route.setFromLatitude(routeDTO.getFromLatitude());
                route.setFromLongitude(routeDTO.getFromLongitude());
                route.setToLatitude(routeDTO.getToLatitude());
                route.setToLongitude(routeDTO.getToLongitude());
                route.setPath(computed.path());
                route.setDistanceKm(computed.distanceKm());
                route.setEstimatedDurationMin(computed.estimatedDurationMin());
            }
            if (routeDTO.getShelterName() != null) route.setShelterName(routeDTO.getShelterName());
            if (routeDTO.getToShelterId() != null) route.setToShelterId(routeDTO.getToShelterId());
            if (routeDTO.getCapacity() != null) route.setCapacity(routeDTO.getCapacity());
            EvacuationRoute updated = evacuationRouteRepository.save(route);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRouteDTO activateRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.ACTIVE);
            route.setActivatedAt(LocalDateTime.now());
            return DTOMapper.toDTO(evacuationRouteRepository.save(route));
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public EvacuationRouteDTO closeRoute(Integer id) {
        return evacuationRouteRepository.findById(id).map(route -> {
            route.setStatus(EvacuationRoute.RouteStatus.CLOSED);
            route.setClosedAt(LocalDateTime.now());
            return DTOMapper.toDTO(evacuationRouteRepository.save(route));
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public void deleteRoute(Integer id) {
        evacuationRouteRepository.deleteById(id);
    }
}
