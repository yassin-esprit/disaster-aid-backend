package tn.esprit.disasteraidbackend.modules.skills.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.esprit.disasteraidbackend.modules.skills.dto.SkillRequestDto;
import tn.esprit.disasteraidbackend.modules.skills.dto.SkillResponseDto;
import tn.esprit.disasteraidbackend.modules.skills.entity.Skill;
import tn.esprit.disasteraidbackend.modules.skills.repository.SkillRepository;

@Service
@RequiredArgsConstructor
public class SkillService {

    private static final Set<String> ALLOWED_CATEGORIES = Set.of(
            "TECHNICAL", "MEDICAL", "LOGISTICS", "EDUCATION", "SOCIAL", "CREATIVE", "LANGUAGE"
    );

    private final SkillRepository repository;

    public List<SkillResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SkillResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<SkillResponseDto> getByCategory(String category) {
        validateCategory(category);
        return repository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SkillResponseDto create(SkillRequestDto dto) {
        validateCategory(dto.getCategory());
        Skill entity = toEntity(dto);
        entity.setSlug(generateSlug(dto.getName()));
        return toResponse(repository.save(entity));
    }

    public SkillResponseDto update(Integer id, SkillRequestDto dto) {
        Skill existing = findById(id);
        validateCategory(dto.getCategory());
        existing.setName(dto.getName());
        existing.setSlug(generateSlug(dto.getName()));
        existing.setCategory(dto.getCategory());
        existing.setParentSkillId(dto.getParentSkillId());
        existing.setDescription(dto.getDescription());
        existing.setIsVerifiable(dto.getIsVerifiable() != null && dto.getIsVerifiable());
        if (dto.getIsActive() != null) {
            existing.setIsActive(dto.getIsActive());
        }
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public SkillResponseDto toggleActive(Integer id) {
        Skill existing = findById(id);
        existing.setIsActive(!existing.getIsActive());
        return toResponse(repository.save(existing));
    }

    private Skill findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found: " + id));
    }

    private Skill toEntity(SkillRequestDto dto) {
        Skill skill = new Skill();
        skill.setName(dto.getName());
        skill.setCategory(dto.getCategory());
        skill.setParentSkillId(dto.getParentSkillId());
        skill.setDescription(dto.getDescription());
        skill.setIsVerifiable(dto.getIsVerifiable() != null && dto.getIsVerifiable());
        skill.setIsActive(dto.getIsActive() == null || dto.getIsActive());
        return skill;
    }

    private SkillResponseDto toResponse(Skill skill) {
        SkillResponseDto dto = new SkillResponseDto();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setSlug(skill.getSlug());
        dto.setCategory(skill.getCategory());
        dto.setParentSkillId(skill.getParentSkillId());
        dto.setDescription(skill.getDescription());
        dto.setIsVerifiable(skill.getIsVerifiable());
        dto.setIsActive(skill.getIsActive());   
        dto.setCreatedAt(skill.getCreatedAt());
        dto.setUpdatedAt(skill.getUpdatedAt());
        return dto;
    }

    private String generateSlug(String name) {
        return name == null ? null : name.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    private void validateCategory(String category) {
        if (!ALLOWED_CATEGORIES.contains(category)) {
            throw new RuntimeException("Invalid skill category: " + category);
        }
    }
}
