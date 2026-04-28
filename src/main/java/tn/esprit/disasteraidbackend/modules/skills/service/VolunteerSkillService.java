package tn.esprit.disasteraidbackend.modules.skills.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.esprit.disasteraidbackend.modules.skills.dto.VolunteerSkillRequestDto;
import tn.esprit.disasteraidbackend.modules.skills.dto.VolunteerSkillResponseDto;
import tn.esprit.disasteraidbackend.modules.skills.entity.Skill;
import tn.esprit.disasteraidbackend.modules.skills.entity.VolunteerSkill;
import tn.esprit.disasteraidbackend.modules.skills.repository.SkillRepository;
import tn.esprit.disasteraidbackend.modules.skills.repository.VolunteerSkillRepository;

@Service
@RequiredArgsConstructor
public class VolunteerSkillService {

    private static final Set<String> ALLOWED_PROFICIENCY = Set.of(
            "BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"
    );

    private final VolunteerSkillRepository repository;
    private final SkillRepository skillRepository;

    public List<VolunteerSkillResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public VolunteerSkillResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<VolunteerSkillResponseDto> getByVolunteerId(Integer volunteerId) {
        return repository.findByVolunteerId(volunteerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<VolunteerSkillResponseDto> getBySkillId(Integer skillId) {
        return repository.findBySkillId(skillId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public VolunteerSkillResponseDto addSkillToVolunteer(VolunteerSkillRequestDto dto) {
        validateProficiency(dto.getProficiency());
        ensureSkillExists(dto.getSkillId());
        repository.findByVolunteerIdAndSkillId(dto.getVolunteerId(), dto.getSkillId())
                .ifPresent(v -> {
                    throw new RuntimeException("Volunteer already has this skill: "
                            + dto.getVolunteerId() + "/" + dto.getSkillId());
                });
        return toResponse(repository.save(toEntity(dto)));
    }

    public VolunteerSkillResponseDto updateProficiency(Integer id, VolunteerSkillRequestDto dto) {
        VolunteerSkill existing = findById(id);
        validateProficiency(dto.getProficiency());
        ensureSkillExists(dto.getSkillId());
        existing.setVolunteerId(dto.getVolunteerId());
        existing.setSkillId(dto.getSkillId());
        existing.setProficiency(dto.getProficiency());
        existing.setVerificationDocUrl(dto.getVerificationDocUrl());
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public VolunteerSkillResponseDto endorse(Integer id) {
        VolunteerSkill existing = findById(id);
        existing.setEndorsementCount(existing.getEndorsementCount() + 1);
        return toResponse(repository.save(existing));
    }

    public VolunteerSkillResponseDto verify(Integer id) {
        VolunteerSkill existing = findById(id);
        existing.setVerified(true);
        return toResponse(repository.save(existing));
    }

    private VolunteerSkill findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerSkill not found: " + id));
    }

    private VolunteerSkill toEntity(VolunteerSkillRequestDto dto) {
        VolunteerSkill volunteerSkill = new VolunteerSkill();
        volunteerSkill.setVolunteerId(dto.getVolunteerId());
        volunteerSkill.setSkillId(dto.getSkillId());
        volunteerSkill.setProficiency(dto.getProficiency());
        volunteerSkill.setVerificationDocUrl(dto.getVerificationDocUrl());
        volunteerSkill.setVerified(false);
        volunteerSkill.setEndorsementCount(0);
        return volunteerSkill;
    }

    private VolunteerSkillResponseDto toResponse(VolunteerSkill volunteerSkill) {
        VolunteerSkillResponseDto dto = new VolunteerSkillResponseDto();
        dto.setId(volunteerSkill.getId());
        dto.setVolunteerId(volunteerSkill.getVolunteerId());
        dto.setSkillId(volunteerSkill.getSkillId());
        dto.setProficiency(volunteerSkill.getProficiency());
        dto.setIsVerified(volunteerSkill.isVerified());
        dto.setVerificationDocUrl(volunteerSkill.getVerificationDocUrl());
        dto.setEndorsementCount(volunteerSkill.getEndorsementCount());
        dto.setCreatedAt(volunteerSkill.getCreatedAt());
        dto.setUpdatedAt(volunteerSkill.getUpdatedAt());
        return dto;
    }

    private void validateProficiency(String proficiency) {
        if (!ALLOWED_PROFICIENCY.contains(proficiency)) {
            throw new RuntimeException("Invalid proficiency value: " + proficiency);
        }
    }

    private void ensureSkillExists(Integer skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found: " + skillId));
        if (!skill.getIsActive()) {
            throw new RuntimeException("Skill is inactive: " + skillId);
        }
    }
}
