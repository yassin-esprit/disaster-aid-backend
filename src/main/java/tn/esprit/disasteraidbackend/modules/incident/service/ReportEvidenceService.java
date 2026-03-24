package tn.esprit.disasteraidbackend.modules.incident.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.entity.ReportEvidence;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;
import tn.esprit.disasteraidbackend.modules.incident.repository.ReportEvidenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportEvidenceService {

    private final ReportEvidenceRepository repository;
    private final IncidentReportRepository reportRepository;

    public List<ReportEvidence> getAll() {
        return repository.findAll();
    }

    public ReportEvidence getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportEvidence not found with id: " + id));
    }

    public List<ReportEvidence> getByReportId(Integer reportId) {
        return repository.findByIncidentReportId(reportId);
    }

    public ReportEvidence create(Integer reportId, ReportEvidence evidence) {
        IncidentReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found with id: " + reportId));
        evidence.setIncidentReport(report);
        return repository.save(evidence);
    }

    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportEvidence not found with id: " + id));
        repository.deleteById(id);
    }
}
