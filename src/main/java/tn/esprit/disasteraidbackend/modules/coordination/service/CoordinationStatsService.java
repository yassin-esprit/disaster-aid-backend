package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.CoordinationStatsDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;
import tn.esprit.disasteraidbackend.modules.coordination.repository.TaskAssignmentRepository;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinationStatsService {

    private final HelpRequestRepository helpRequestRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final IncidentReportRepository incidentReportRepository;

    public CoordinationStatsDto getStats() {

        // Open help requests
        long openRequests = helpRequestRepository.findByStatus("OPEN").size()
                + helpRequestRepository.findByStatus("ASSIGNED").size();

        // Resolved today
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        long resolvedToday = helpRequestRepository.findByStatus("RESOLVED")
                .stream()
                .filter(r -> r.getUpdatedAt() != null && r.getUpdatedAt().isAfter(startOfDay))
                .count();

        // Active assignments
        long activeAssignments = taskAssignmentRepository.findByStatus("ASSIGNED").size()
                + taskAssignmentRepository.findByStatus("ACCEPTED").size();

        // Average response time (only from assignments that have it recorded)
        List<TaskAssignment> allAssignments = taskAssignmentRepository.findAll();
        double avgResponseTime = allAssignments.stream()
                .filter(a -> a.getResponseTimeMin() != null && a.getResponseTimeMin() > 0)
                .mapToInt(TaskAssignment::getResponseTimeMin)
                .average()
                .orElse(0.0);

        // Pending incidents
        long pendingIncidents = incidentReportRepository.findByStatus("PENDING").size();

        // Average credibility
        double avgCredibility = incidentReportRepository.findAll()
                .stream()
                .filter(r -> r.getCredibilityScore() != null && r.getCredibilityScore() > 0)
                .mapToDouble(r -> r.getCredibilityScore())
                .average()
                .orElse(0.0);

        return new CoordinationStatsDto(
                openRequests,
                resolvedToday,
                activeAssignments,
                Math.round(avgResponseTime * 10.0) / 10.0,
                pendingIncidents,
                Math.round(avgCredibility * 100.0) / 100.0
        );
    }
}