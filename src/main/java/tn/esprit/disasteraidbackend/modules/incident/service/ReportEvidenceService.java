package tn.esprit.disasteraidbackend.modules.incident.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.dto.ReportEvidenceRequestDto;
import tn.esprit.disasteraidbackend.modules.incident.dto.ReportEvidenceResponseDto;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.entity.ReportEvidence;
import tn.esprit.disasteraidbackend.modules.incident.repository.IncidentReportRepository;
import tn.esprit.disasteraidbackend.modules.incident.repository.ReportEvidenceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportEvidenceService {

    private final ReportEvidenceRepository repository;
    private final IncidentReportRepository reportRepository;

    public List<ReportEvidenceResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReportEvidenceResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<ReportEvidenceResponseDto> getByReportId(Integer reportId) {
        return repository.findByIncidentReportId(reportId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReportEvidenceResponseDto create(Integer reportId, ReportEvidenceRequestDto dto) {
        IncidentReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("IncidentReport not found with id: " + reportId));
        ReportEvidence evidence = toEntity(dto);
        evidence.setIncidentReport(report);
        return toResponse(repository.save(evidence));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private ReportEvidence findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportEvidence not found with id: " + id));
    }

    private ReportEvidence toEntity(ReportEvidenceRequestDto dto) {
        ReportEvidence evidence = new ReportEvidence();
        evidence.setFileUrl(dto.getFileUrl());
        evidence.setFileType(dto.getFileType());
        evidence.setClipScore(dto.getClipScore());
        evidence.setCapturedAt(dto.getCapturedAt());
        return evidence;
    }

    private ReportEvidenceResponseDto toResponse(ReportEvidence evidence) {
        ReportEvidenceResponseDto dto = new ReportEvidenceResponseDto();
        dto.setId(evidence.getId());
        dto.setIncidentReportId(evidence.getIncidentReport().getId());
        dto.setFileUrl(evidence.getFileUrl());
        dto.setFileType(evidence.getFileType());
        dto.setClipScore(evidence.getClipScore());
        dto.setCapturedAt(evidence.getCapturedAt());
        dto.setCreatedAt(evidence.getCreatedAt());
        return dto;
    }
}