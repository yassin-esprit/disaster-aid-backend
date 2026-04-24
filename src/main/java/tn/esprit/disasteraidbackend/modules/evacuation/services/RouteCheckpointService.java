package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.evacuation.dto.RouteCheckpointDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.RouteCheckpointRepository;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.EvacuationRouteRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteCheckpointService {

    private final RouteCheckpointRepository checkpointRepository;
    private final EvacuationRouteRepository evacuationRouteRepository;
    private final GeometryFactory geometryFactory;

    public RouteCheckpointDTO createCheckpoint(RouteCheckpointDTO checkpointDTO) {
        RouteCheckpoint checkpoint = DTOMapper.toEntity(checkpointDTO, geometryFactory);
        EvacuationRoute route = evacuationRouteRepository.findById(checkpointDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));
        checkpoint.setRoute(route);
        RouteCheckpoint saved = checkpointRepository.save(checkpoint);
        return DTOMapper.toDTO(saved);
    }

    public Optional<RouteCheckpointDTO> getCheckpointById(Integer id) {
        return checkpointRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<RouteCheckpointDTO> getCheckpointsByRouteId(Integer routeId) {
        EvacuationRoute route = evacuationRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        return checkpointRepository.findByRoute(route).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RouteCheckpointDTO> getCheckpointsByType(RouteCheckpoint.CheckpointType type) {
        return checkpointRepository.findByType(type).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RouteCheckpointDTO updateCheckpoint(Integer id, RouteCheckpointDTO checkpointDTO) {
        return checkpointRepository.findById(id).map(cp -> {
            cp.setLocation((Point) DTOMapper.fromWKT(checkpointDTO.getLocation(), geometryFactory));
            cp.setLabel(checkpointDTO.getLabel());
            cp.setType(checkpointDTO.getType());
            cp.setResponderIds(checkpointDTO.getResponderIds());
            cp.setIsActive(checkpointDTO.getIsActive());
            RouteCheckpoint updated = checkpointRepository.save(cp);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Checkpoint not found"));
    }

    public void deleteCheckpoint(Integer id) {
        checkpointRepository.deleteById(id);
    }

    public List<RouteCheckpointDTO> getAllCheckpoints() {
        return checkpointRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
