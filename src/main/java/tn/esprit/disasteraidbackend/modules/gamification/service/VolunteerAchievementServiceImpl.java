package tn.esprit.disasteraidbackend.modules.gamification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.VolunteerAchievement;
import tn.esprit.disasteraidbackend.modules.gamification.repository.VolunteerAchievementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerAchievementServiceImpl implements VolunteerAchievementService {

    private final VolunteerAchievementRepository volunteerAchievementRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private VolunteerAchievementResponse toResponse(VolunteerAchievement va) {
        VolunteerAchievementResponse response = new VolunteerAchievementResponse();
        response.setVaId(va.getVaId());
        response.setVolunteerId(va.getVolunteerId());
        response.setAchievementId(va.getAchievementId());
        response.setEarnedAt(va.getEarnedAt());
        response.setIsDisplayed(va.getIsDisplayed());
        return response;
    }

    // ── Operations ───────────────────────────────────────────────────────────

    @Override
    public VolunteerAchievementResponse awardAchievement(VolunteerAchievementRequest request) {
        // Prevent duplicate awards
        if (volunteerAchievementRepository.existsByVolunteerIdAndAchievementId(
                request.getVolunteerId(), request.getAchievementId())) {
            throw new RuntimeException(
                    "Volunteer " + request.getVolunteerId()
                    + " already has achievement " + request.getAchievementId());
        }

        VolunteerAchievement va = VolunteerAchievement.builder()
                .volunteerId(request.getVolunteerId())
                .achievementId(request.getAchievementId())
                .earnedAt(LocalDateTime.now())
                .isDisplayed(request.getIsDisplayed() != null ? request.getIsDisplayed() : true)
                .build();

        return toResponse(volunteerAchievementRepository.save(va));
    }

    @Override
    public VolunteerAchievementResponse getById(Long id) {
        VolunteerAchievement va = volunteerAchievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerAchievement not found with id: " + id));
        return toResponse(va);
    }

    @Override
    public List<VolunteerAchievementResponse> getAll() {
        return volunteerAchievementRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<VolunteerAchievementResponse> getByVolunteer(Long volunteerId) {
        return volunteerAchievementRepository.findByVolunteerId(volunteerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<VolunteerAchievementResponse> getDisplayedByVolunteer(Long volunteerId) {
        return volunteerAchievementRepository.findByVolunteerIdAndIsDisplayed(volunteerId, true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public VolunteerAchievementResponse toggleDisplay(Long id, boolean isDisplayed) {
        VolunteerAchievement va = volunteerAchievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerAchievement not found with id: " + id));
        va.setIsDisplayed(isDisplayed);
        return toResponse(volunteerAchievementRepository.save(va));
    }

    @Override
    public void delete(Long id) {
        if (!volunteerAchievementRepository.existsById(id)) {
            throw new RuntimeException("VolunteerAchievement not found with id: " + id);
        }
        volunteerAchievementRepository.deleteById(id);
    }
}
