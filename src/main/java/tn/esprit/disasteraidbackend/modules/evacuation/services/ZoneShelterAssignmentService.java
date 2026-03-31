package tn.esprit.disasteraidbackend.modules.evacuation.services;

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

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneShelterAssignmentService {

    private final ZoneShelterAssignmentRepository assignmentRepository;
    private final GeofenceZoneRepository geofenceZoneRepository;

    public ZoneShelterAssignment createAssignment(ZoneShelterAssignment assignment) {
        // Validate zone exists
        GeofenceZone zone = geofenceZoneRepository.findById(assignment.getGeofenceZone().getId())
                .orElseThrow(() -> new RuntimeException("Geofence zone not found"));
        assignment.setGeofenceZone(zone);
        assignment.setCreatedAt(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }

    public Optional<ZoneShelterAssignment> getAssignmentById(Integer id) {
        return assignmentRepository.findById(id);
    }

    public List<ZoneShelterAssignment> getAssignmentsByZone(GeofenceZone zone) {
        return assignmentRepository.findByGeofenceZone(zone);
    }

    public List<ZoneShelterAssignment> getAssignmentsByShelter(Integer shelterId) {
        return assignmentRepository.findByShelterId(shelterId);
    }

    public ZoneShelterAssignment updateAssignment(Integer id, ZoneShelterAssignment updated) {
        return assignmentRepository.findById(id).map(assignment -> {
            assignment.setPriority(updated.getPriority());
            assignment.setEstimatedUsersCount(updated.getEstimatedUsersCount());
            return assignmentRepository.save(assignment);
        }).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public void deleteAssignment(Integer id) {
        assignmentRepository.deleteById(id);
    }

    public List<ZoneShelterAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}