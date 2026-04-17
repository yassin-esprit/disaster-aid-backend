package tn.esprit.disasteraidbackend.modules.incident.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportRequestDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentReportResponseDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.IncidentStatusHistoryDto;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentStatusHistory;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentStatusHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentReportService {

    private final IncidentReportRepository repository;
    private final IncidentStatusHistoryRepository historyRepository;
    private final CredibilityService credibilityService;

    public List<IncidentReportResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public IncidentReportResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public IncidentReportResponseDto create(IncidentReportRequestDto dto) {
        IncidentReport report = toEntity(dto);
        report.setStatus("PENDING");
        report.setUpvoteCount(0);
        report.setCredibilityScore(0.0);
        IncidentReport saved = repository.save(report);
        saved.setCredibilityScore(credibilityService.compute(saved));
        IncidentReport final_ = repository.save(saved);
        recordHistory(final_.getId(), null, "PENDING", null, "Report created");
        return toResponse(final_);
    }

    public IncidentReportResponseDto update(Integer id, IncidentReportRequestDto dto) {
        IncidentReport existing = findById(id);
        String oldStatus = existing.getStatus();
        existing.setDisasterType(dto.getDisasterType());
        existing.setSeverity(dto.getSeverity());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        existing.setAlertId(dto.getAlertId());
        if (dto.getStatus() != null && !dto.getStatus().equals(oldStatus)) {
            existing.setStatus(dto.getStatus());
            recordHistory(id, oldStatus, dto.getStatus(), null, null);
        }
        existing.setCredibilityScore(credibilityService.compute(existing));
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<IncidentReportResponseDto> getByStatus(String status) {
        return repository.findByStatus(status).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<IncidentReportResponseDto> getBySeverity(String severity) {
        return repository.findBySeverity(severity).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public IncidentReportResponseDto upvote(Integer id) {
        IncidentReport report = findById(id);
        report.setUpvoteCount(report.getUpvoteCount() + 1);
        report.setCredibilityScore(credibilityService.compute(report));
        return toResponse(repository.save(report));
    }

    public List<IncidentStatusHistoryDto> getHistory(Integer id) {
        return historyRepository
                .findByIncidentReportIdOrderByChangedAtAsc(id)
                .stream()
                .map(this::toHistoryDto)
                .collect(Collectors.toList());
    }

    // ── Private helpers ─────────────────────────
    private void recordHistory(Integer reportId, String from, String to,
                               Integer changedBy, String note) {
        IncidentStatusHistory h = new IncidentStatusHistory();
        h.setIncidentReportId(reportId);
        h.setFromStatus(from);
        h.setToStatus(to);
        h.setChangedBy(changedBy);
        h.setNote(note);
        historyRepository.save(h);
    }

    private IncidentReport findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found: " + id));
    }

    private IncidentReport toEntity(IncidentReportRequestDto dto) {
        IncidentReport r = new IncidentReport();
        r.setReporterId(dto.getReporterId());
        r.setAlertId(dto.getAlertId());
        r.setDisasterType(dto.getDisasterType());
        r.setSeverity(dto.getSeverity());
        r.setLocation(dto.getLocation());
        r.setDescription(dto.getDescription());
        return r;
    }

    private IncidentReportResponseDto toResponse(IncidentReport r) {
        IncidentReportResponseDto dto = new IncidentReportResponseDto();
        dto.setId(r.getId());
        dto.setReporterId(r.getReporterId());
        dto.setAlertId(r.getAlertId());
        dto.setDisasterType(r.getDisasterType());
        dto.setSeverity(r.getSeverity());
        dto.setLocation(r.getLocation());
        dto.setDescription(r.getDescription());
        dto.setStatus(r.getStatus());
        dto.setCredibilityScore(r.getCredibilityScore());
        dto.setUpvoteCount(r.getUpvoteCount());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        return dto;
    }

    private IncidentStatusHistoryDto toHistoryDto(IncidentStatusHistory h) {
        IncidentStatusHistoryDto dto = new IncidentStatusHistoryDto();
        dto.setId(h.getId());
        dto.setFromStatus(h.getFromStatus());
        dto.setToStatus(h.getToStatus());
        dto.setChangedBy(h.getChangedBy());
        dto.setNote(h.getNote());
        dto.setChangedAt(h.getChangedAt());
        return dto;
    }
}