package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.HelpRequestRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.HelpRequestResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

    private final HelpRequestRepository repository;

    public List<HelpRequestResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public HelpRequestResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public HelpRequestResponseDto create(HelpRequestRequestDto dto) {
        HelpRequest request = toEntity(dto);
        request.setStatus("OPEN");
        return toResponse(repository.save(request));
    }

    public HelpRequestResponseDto update(Integer id, HelpRequestRequestDto dto) {
        HelpRequest existing = findById(id);
        existing.setType(dto.getType());
        existing.setUrgency(dto.getUrgency());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        existing.setAlertId(dto.getAlertId());
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<HelpRequestResponseDto> getByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private HelpRequest findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found with id: " + id));
    }

    private HelpRequest toEntity(HelpRequestRequestDto dto) {
        HelpRequest request = new HelpRequest();
        request.setReporterId(dto.getReporterId());
        request.setAlertId(dto.getAlertId());
        request.setType(dto.getType());
        request.setUrgency(dto.getUrgency());
        request.setLocation(dto.getLocation());
        request.setDescription(dto.getDescription());
        return request;
    }

    private HelpRequestResponseDto toResponse(HelpRequest request) {
        HelpRequestResponseDto dto = new HelpRequestResponseDto();
        dto.setId(request.getId());
        dto.setReporterId(request.getReporterId());
        dto.setAlertId(request.getAlertId());
        dto.setType(request.getType());
        dto.setUrgency(request.getUrgency());
        dto.setLocation(request.getLocation());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setAssignedResponderId(request.getAssignedResponderId());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}