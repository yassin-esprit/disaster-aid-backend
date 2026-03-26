package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

    private final HelpRequestRepository repository;

    public List<HelpRequest> getAll() {
        return repository.findAll();
    }

    public HelpRequest getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found with id: " + id));
    }

    public HelpRequest create(HelpRequest request) {
        request.setStatus("OPEN");
        return repository.save(request);
    }

    public HelpRequest update(Integer id, HelpRequest updated) {
        HelpRequest existing = getById(id);
        existing.setType(updated.getType());
        existing.setUrgency(updated.getUrgency());
        existing.setLocation(updated.getLocation());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setAssignedResponderId(updated.getAssignedResponderId());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found with id: " + id));
        repository.deleteById(id);
    }

    public List<HelpRequest> getByStatus(String status) {
        return repository.findByStatus(status);
    }
}
