package tn.esprit.disasteraidbackend.modules.incident.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentReportService {

    private final IncidentReportRepository repository;

    public List<IncidentReport> getAll() {
        return repository.findAll();
    }

    public IncidentReport getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found with id: " + id));
    }

    public IncidentReport create(IncidentReport report) {
        report.setStatus("PENDING");
        report.setCredibilityScore(0.0);
        report.setUpvoteCount(0);
        return repository.save(report);
    }

    public IncidentReport update(Integer id, IncidentReport updated) {
        IncidentReport existing = getById(id);
        existing.setDisasterType(updated.getDisasterType());
        existing.setSeverity(updated.getSeverity());
        existing.setLocation(updated.getLocation());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setCredibilityScore(updated.getCredibilityScore());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found with id: " + id));
        repository.deleteById(id);
    }

    public List<IncidentReport> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<IncidentReport> getBySeverity(String severity) {
        return repository.findBySeverity(severity);
    }
}