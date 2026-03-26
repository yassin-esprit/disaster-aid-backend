package tn.esprit.disasteraidbackend.modules.coordination.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;
import tn.esprit.disasteraidbackend.modules.coordination.repository.TaskAssignmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAssignmentService {

    private final TaskAssignmentRepository repository;
    private final HelpRequestRepository helpRequestRepository;

    public List<TaskAssignment> getAll() {
        return repository.findAll();
    }

    public TaskAssignment getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskAssignment not found with id: " + id));
    }

    public List<TaskAssignment> getByHelpRequestId(Integer helpRequestId) {
        return repository.findByHelpRequestId(helpRequestId);
    }

    public TaskAssignment create(Integer helpRequestId, TaskAssignment assignment) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found with id: " + helpRequestId));
        assignment.setHelpRequest(helpRequest);
        assignment.setStatus("ASSIGNED");
        return repository.save(assignment);
    }

    public TaskAssignment updateStatus(Integer id, String status) {
        TaskAssignment assignment = getById(id);
        assignment.setStatus(status);
        return repository.save(assignment);
    }

    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskAssignment not found with id: " + id));
        repository.deleteById(id);
    }
}