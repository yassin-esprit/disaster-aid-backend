package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.CompleteAssignmentDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.TaskAssignmentRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.TaskAssignmentResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;
import tn.esprit.disasteraidbackend.modules.coordination.repository.TaskAssignmentRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskAssignmentService {

    private final TaskAssignmentRepository repository;
    private final HelpRequestRepository helpRequestRepository;
    private final MlMatchingService mlMatchingService;

    public List<TaskAssignmentResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskAssignmentResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<TaskAssignmentResponseDto> getByHelpRequestId(Integer helpRequestId) {
        return repository.findByHelpRequestId(helpRequestId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskAssignmentResponseDto create(Integer helpRequestId, TaskAssignmentRequestDto dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found: " + helpRequestId));
        TaskAssignment assignment = toEntity(dto);
        assignment.setHelpRequest(helpRequest);
        assignment.setStatus("ASSIGNED");

        // Compute ML matching score
        double mlScore = mlMatchingService.computeScore(helpRequest, dto.getResponderId());
        assignment.setMlScoreAtAssignment(mlScore);

        return toResponse(repository.save(assignment));
    }

    public TaskAssignmentResponseDto updateStatus(Integer id, String status) {
        TaskAssignment assignment = findById(id);
        String previousStatus = assignment.getStatus();
        assignment.setStatus(status);

        // Record response time when responder accepts
        if ("ACCEPTED".equals(status) && !"ACCEPTED".equals(previousStatus)) {
            if (assignment.getCreatedAt() != null) {
                long minutes = ChronoUnit.MINUTES.between(assignment.getCreatedAt(), LocalDateTime.now());
                assignment.setResponseTimeMin((int) minutes);
            }
        }

        return toResponse(repository.save(assignment));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private TaskAssignment findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskAssignment not found: " + id));
    }

    private TaskAssignment toEntity(TaskAssignmentRequestDto dto) {
        TaskAssignment a = new TaskAssignment();
        a.setResponderId(dto.getResponderId());
        a.setMlScoreAtAssignment(dto.getMlScoreAtAssignment());
        a.setNotes(dto.getNotes());
        return a;
    }


    private TaskAssignmentResponseDto toResponse(TaskAssignment a) {
        TaskAssignmentResponseDto dto = new TaskAssignmentResponseDto();
        dto.setId(a.getId());
        dto.setHelpRequestId(a.getHelpRequest().getId());
        dto.setResponderId(a.getResponderId());
        dto.setStatus(a.getStatus());
        dto.setMlScoreAtAssignment(a.getMlScoreAtAssignment());
        dto.setResponseTimeMin(a.getResponseTimeMin());
        dto.setNotes(a.getNotes());
        dto.setCompletionNote(a.getCompletionNote());
        dto.setRating(a.getRating());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        return dto;
    }

    public TaskAssignmentResponseDto completeAssignment(Integer id, CompleteAssignmentDto dto) {
        TaskAssignment assignment = findById(id);
        String previousStatus = assignment.getStatus();

        if (dto.getRating() != null && (dto.getRating() < 1 || dto.getRating() > 5)) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        assignment.setStatus("COMPLETED");
        assignment.setCompletionNote(dto.getCompletionNote());
        assignment.setRating(dto.getRating());

        // Record response time if not already set
        if (assignment.getResponseTimeMin() == null && assignment.getCreatedAt() != null) {
            long minutes = ChronoUnit.MINUTES.between(assignment.getCreatedAt(), LocalDateTime.now());
            assignment.setResponseTimeMin((int) minutes);
        }

        return toResponse(repository.save(assignment));
    }
}