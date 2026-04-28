package tn.esprit.disasteraidbackend.modules.opportunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityRequestDto;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityResponseDto;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.Opportunity;
import tn.esprit.disasteraidbackend.modules.opportunity.repository.OpportunityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private static final Set<String> ALLOWED_STATUS = Set.of("DRAFT", "OPEN", "CLOSED", "CANCELLED", "COMPLETED");

    private final OpportunityRepository repository;

    public List<OpportunityResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OpportunityResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<OpportunityResponseDto> getByStatus(String status) {
        validateStatus(status);
        return repository.findByStatus(status).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OpportunityResponseDto> getByOrgId(Integer orgId) {
        return repository.findByOrgId(orgId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OpportunityResponseDto> getByCategory(String category) {
        return repository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OpportunityResponseDto create(OpportunityRequestDto dto) {
        Opportunity entity = toEntity(dto);
        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            entity.setStatus("DRAFT");
        } else {
            validateStatus(dto.getStatus());
            entity.setStatus(dto.getStatus());
        }
        return toResponse(repository.save(entity));
    }

    public OpportunityResponseDto update(Integer id, OpportunityRequestDto dto) {
        Opportunity existing = findById(id);
        existing.setOrgId(dto.getOrgId());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setLocation(dto.getLocation());
        existing.setRemote(Boolean.TRUE.equals(dto.getIsRemote()));
        existing.setScheduledDate(dto.getScheduledDate());
        existing.setDurationHours(dto.getDurationHours());
        existing.setSlots(dto.getSlots());
        existing.setMinAge(dto.getMinAge());
        existing.setRequiresBackgroundCheck(Boolean.TRUE.equals(dto.getRequiresBackgroundCheck()));
        existing.setRequiredSkillIds(dto.getRequiredSkillIds() == null ? new ArrayList<>() : dto.getRequiredSkillIds());

        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            validateStatus(dto.getStatus());
            existing.setStatus(dto.getStatus());
        }

        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public OpportunityResponseDto closeOpportunity(Integer id) {
        Opportunity existing = findById(id);
        existing.setStatus("CLOSED");
        existing.setClosedAt(LocalDateTime.now());
        return toResponse(repository.save(existing));
    }

    private Opportunity findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found: " + id));
    }

    private Opportunity toEntity(OpportunityRequestDto dto) {
        Opportunity entity = new Opportunity();
        entity.setOrgId(dto.getOrgId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setLocation(dto.getLocation());
        entity.setRemote(Boolean.TRUE.equals(dto.getIsRemote()));
        entity.setScheduledDate(dto.getScheduledDate());
        entity.setDurationHours(dto.getDurationHours());
        entity.setSlots(dto.getSlots());
        entity.setMinAge(dto.getMinAge());
        entity.setRequiresBackgroundCheck(Boolean.TRUE.equals(dto.getRequiresBackgroundCheck()));
        entity.setRequiredSkillIds(dto.getRequiredSkillIds() == null ? new ArrayList<>() : dto.getRequiredSkillIds());
        entity.setFilledSlots(0);
        return entity;
    }

    private OpportunityResponseDto toResponse(Opportunity entity) {
        OpportunityResponseDto dto = new OpportunityResponseDto();
        dto.setId(entity.getId());
        dto.setOrgId(entity.getOrgId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setLocation(entity.getLocation());
        dto.setRemote(entity.isRemote());
        dto.setScheduledDate(entity.getScheduledDate());
        dto.setDurationHours(entity.getDurationHours());
        dto.setSlots(entity.getSlots());
        dto.setFilledSlots(entity.getFilledSlots());
        dto.setStatus(entity.getStatus());
        dto.setMinAge(entity.getMinAge());
        dto.setRequiresBackgroundCheck(entity.isRequiresBackgroundCheck());
        dto.setRequiredSkillIds(entity.getRequiredSkillIds());
        dto.setClosedAt(entity.getClosedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUS.contains(status)) {
            throw new RuntimeException("Invalid opportunity status: " + status);
        }
    }
}
