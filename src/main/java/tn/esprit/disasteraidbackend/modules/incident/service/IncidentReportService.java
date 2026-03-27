package tn.esprit.disasteraidbackend.modules.incident.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportRequestDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportResponseDto;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentReportService {

    private final IncidentReportRepository repository;

    public List<IncidentReportResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public IncidentReportResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public IncidentReportResponseDto create(IncidentReportRequestDto dto) {
        IncidentReport report = toEntity(dto);
        report.setStatus("PENDING");
        report.setCredibilityScore(0.0);
        report.setUpvoteCount(0);
        return toResponse(repository.save(report));
    }

    public IncidentReportResponseDto update(Integer id, IncidentReportRequestDto dto) {
        IncidentReport existing = findById(id);
        existing.setDisasterType(dto.getDisasterType());
        existing.setSeverity(dto.getSeverity());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        existing.setAlertId(dto.getAlertId());
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<IncidentReportResponseDto> getByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<IncidentReportResponseDto> getBySeverity(String severity) {
        return repository.findBySeverity(severity)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private IncidentReport findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found with id: " + id));
    }

    private IncidentReport toEntity(IncidentReportRequestDto dto) {
        IncidentReport report = new IncidentReport();
        report.setReporterId(dto.getReporterId());
        report.setAlertId(dto.getAlertId());
        report.setDisasterType(dto.getDisasterType());
        report.setSeverity(dto.getSeverity());
        report.setLocation(dto.getLocation());
        report.setDescription(dto.getDescription());
        return report;
    }

    private IncidentReportResponseDto toResponse(IncidentReport report) {
        IncidentReportResponseDto dto = new IncidentReportResponseDto();
        dto.setId(report.getId());
        dto.setReporterId(report.getReporterId());
        dto.setAlertId(report.getAlertId());
        dto.setDisasterType(report.getDisasterType());
        dto.setSeverity(report.getSeverity());
        dto.setLocation(report.getLocation());
        dto.setDescription(report.getDescription());
        dto.setStatus(report.getStatus());
        dto.setCredibilityScore(report.getCredibilityScore());
        dto.setUpvoteCount(report.getUpvoteCount());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        return dto;
    }
}