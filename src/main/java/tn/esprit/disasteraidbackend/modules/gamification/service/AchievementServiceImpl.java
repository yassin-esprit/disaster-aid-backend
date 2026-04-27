package tn.esprit.disasteraidbackend.modules.gamification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Achievement;
import tn.esprit.disasteraidbackend.modules.gamification.entity.AchievementCategory;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Rarity;
import tn.esprit.disasteraidbackend.modules.gamification.repository.AchievementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private Achievement toEntity(AchievementRequest request) {
        return Achievement.builder()
                .name(request.getName())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .category(request.getCategory())
                .criteria(request.getCriteria())
                .points(request.getPoints())
                .rarity(request.getRarity())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    private AchievementResponse toResponse(Achievement a) {
        AchievementResponse response = new AchievementResponse();
        response.setAchievementId(a.getAchievementId());
        response.setName(a.getName());
        response.setDescription(a.getDescription());
        response.setIconUrl(a.getIconUrl());
        response.setCategory(a.getCategory());
        response.setCriteria(a.getCriteria());
        response.setPoints(a.getPoints());
        response.setRarity(a.getRarity());
        response.setIsActive(a.getIsActive());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public AchievementResponse createAchievement(AchievementRequest request) {
        Achievement saved = achievementRepository.save(toEntity(request));
        return toResponse(saved);
    }

    @Override
    public AchievementResponse getAchievementById(Long id) {
        Achievement a = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));
        return toResponse(a);
    }

    @Override
    public List<AchievementResponse> getAllAchievements() {
        return achievementRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<AchievementResponse> getByCategory(AchievementCategory category) {
        return achievementRepository.findByCategory(category)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<AchievementResponse> getByRarity(Rarity rarity) {
        return achievementRepository.findByRarity(rarity)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<AchievementResponse> getActiveAchievements() {
        return achievementRepository.findByIsActive(true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public AchievementResponse updateAchievement(Long id, AchievementRequest request) {
        Achievement a = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));
        a.setName(request.getName());
        a.setDescription(request.getDescription());
        a.setIconUrl(request.getIconUrl());
        a.setCategory(request.getCategory());
        a.setCriteria(request.getCriteria());
        a.setPoints(request.getPoints());
        a.setRarity(request.getRarity());
        if (request.getIsActive() != null) a.setIsActive(request.getIsActive());
        return toResponse(achievementRepository.save(a));
    }

    @Override
    public void deleteAchievement(Long id) {
        if (!achievementRepository.existsById(id)) {
            throw new RuntimeException("Achievement not found with id: " + id);
        }
        achievementRepository.deleteById(id);
    }
}
