package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.TaskAssignmentRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.TaskAssignmentResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;
import tn.esprit.disasteraidbackend.modules.coordination.repository.TaskAssignmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskAssignmentService {

    private final TaskAssignmentRepository repository;
    private final HelpRequestRepository helpRequestRepository;

    public List<TaskAssignmentResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskAssignmentResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<TaskAssignmentResponseDto> getByHelpRequestId(Integer helpRequestId) {
        return repository.findByHelpRequestId(helpRequestId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskAssignmentResponseDto create(Integer helpRequestId, TaskAssignmentRequestDto dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found with id: " + helpRequestId));
        TaskAssignment assignment = toEntity(dto);
        assignment.setHelpRequest(helpRequest);
        assignment.setStatus("ASSIGNED");
        return toResponse(repository.save(assignment));
    }

    public TaskAssignmentResponseDto updateStatus(Integer id, String status) {
        TaskAssignment assignment = findById(id);
        assignment.setStatus(status);
        return toResponse(repository.save(assignment));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private TaskAssignment findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskAssignment not found with id: " + id));
    }

    private TaskAssignment toEntity(TaskAssignmentRequestDto dto) {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setResponderId(dto.getResponderId());
        assignment.setMlScoreAtAssignment(dto.getMlScoreAtAssignment());
        assignment.setNotes(dto.getNotes());
        return assignment;
    }

    private TaskAssignmentResponseDto toResponse(TaskAssignment assignment) {
        TaskAssignmentResponseDto dto = new TaskAssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setHelpRequestId(assignment.getHelpRequest().getId());
        dto.setResponderId(assignment.getResponderId());
        dto.setStatus(assignment.getStatus());
        dto.setMlScoreAtAssignment(assignment.getMlScoreAtAssignment());
        dto.setResponseTimeMin(assignment.getResponseTimeMin());
        dto.setNotes(assignment.getNotes());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setUpdatedAt(assignment.getUpdatedAt());
        return dto;
    }
}