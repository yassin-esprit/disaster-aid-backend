package tn.esprit.disasteraidbackend.modules.evacuation.services;

import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.evacuation.dto.ZoneShelterAssignmentDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.ZoneShelterAssignment;
import tn.esprit.disasteraidbackend.modules.evacuation.repositories.ZoneShelterAssignmentRepository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.GeofenceZoneRepository;
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
public class ZoneShelterAssignmentService {

    private final ZoneShelterAssignmentRepository assignmentRepository;
    private final GeofenceZoneRepository geofenceZoneRepository;

    public ZoneShelterAssignmentDTO createAssignment(ZoneShelterAssignmentDTO assignmentDTO) {
        // Convert DTO to entity
        ZoneShelterAssignment assignment = DTOMapper.toEntity(assignmentDTO);
        // Fetch related zone
        GeofenceZone zone = geofenceZoneRepository.findById(assignmentDTO.getGeofenceZoneId())
                .orElseThrow(() -> new RuntimeException("Geofence zone not found"));
        assignment.setGeofenceZone(zone);
        assignment.setCreatedAt(LocalDateTime.now());
        ZoneShelterAssignment saved = assignmentRepository.save(assignment);
        return DTOMapper.toDTO(saved);
    }

    public Optional<ZoneShelterAssignmentDTO> getAssignmentById(Integer id) {
        return assignmentRepository.findById(id)
                .map(DTOMapper::toDTO);
    }

    public List<ZoneShelterAssignmentDTO> getAssignmentsByZoneId(Integer zoneId) {
        GeofenceZone zone = geofenceZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Geofence zone not found"));
        return assignmentRepository.findByGeofenceZone(zone).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ZoneShelterAssignmentDTO> getAssignmentsByShelter(Integer shelterId) {
        return assignmentRepository.findByShelterId(shelterId).stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ZoneShelterAssignmentDTO updateAssignment(Integer id, ZoneShelterAssignmentDTO assignmentDTO) {
        return assignmentRepository.findById(id).map(assignment -> {
            assignment.setPriority(assignmentDTO.getPriority());
            assignment.setEstimatedUsersCount(assignmentDTO.getEstimatedUsersCount());
            ZoneShelterAssignment updated = assignmentRepository.save(assignment);
            return DTOMapper.toDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public void deleteAssignment(Integer id) {
        assignmentRepository.deleteById(id);
    }

    public List<ZoneShelterAssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}