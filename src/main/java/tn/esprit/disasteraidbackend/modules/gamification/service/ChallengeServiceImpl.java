package tn.esprit.disasteraidbackend.modules.gamification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Challenge;
import tn.esprit.disasteraidbackend.modules.gamification.entity.ChallengeStatus;
import tn.esprit.disasteraidbackend.modules.gamification.repository.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private Challenge toEntity(ChallengeRequest request) {
        return Challenge.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .targetMetric(request.getTargetMetric())
                .targetValue(request.getTargetValue())
                .rewardPoints(request.getRewardPoints())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .participantIds(request.getParticipantIds() != null
                        ? request.getParticipantIds() : new ArrayList<>())
                .status(request.getStatus() != null
                        ? request.getStatus() : ChallengeStatus.UPCOMING)
                .build();
    }

    private ChallengeResponse toResponse(Challenge c) {
        ChallengeResponse response = new ChallengeResponse();
        response.setChallengeId(c.getChallengeId());
        response.setTitle(c.getTitle());
        response.setDescription(c.getDescription());
        response.setTargetMetric(c.getTargetMetric());
        response.setTargetValue(c.getTargetValue());
        response.setRewardPoints(c.getRewardPoints());
        response.setStartDate(c.getStartDate());
        response.setEndDate(c.getEndDate());
        response.setParticipantIds(c.getParticipantIds());
        response.setStatus(c.getStatus());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public ChallengeResponse createChallenge(ChallengeRequest request) {
        return toResponse(challengeRepository.save(toEntity(request)));
    }

    @Override
    public ChallengeResponse getChallengeById(Long id) {
        Challenge c = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + id));
        return toResponse(c);
    }

    @Override
    public List<ChallengeResponse> getAllChallenges() {
        return challengeRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeResponse> getByStatus(ChallengeStatus status) {
        return challengeRepository.findByStatus(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeResponse> getChallengesByVolunteer(Long volunteerId) {
        return challengeRepository.findByParticipantId(volunteerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ChallengeResponse updateChallenge(Long id, ChallengeRequest request) {
        Challenge c = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + id));
        c.setTitle(request.getTitle());
        c.setDescription(request.getDescription());
        c.setTargetMetric(request.getTargetMetric());
        c.setTargetValue(request.getTargetValue());
        c.setRewardPoints(request.getRewardPoints());
        c.setStartDate(request.getStartDate());
        c.setEndDate(request.getEndDate());
        if (request.getStatus() != null) c.setStatus(request.getStatus());
        if (request.getParticipantIds() != null) c.setParticipantIds(request.getParticipantIds());
        return toResponse(challengeRepository.save(c));
    }

    @Override
    public ChallengeResponse joinChallenge(Long challengeId, Long volunteerId) {
        Challenge c = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + challengeId));
        List<Long> participants = c.getParticipantIds();
        if (participants == null) participants = new ArrayList<>();
        if (participants.contains(volunteerId)) {
            throw new RuntimeException("Volunteer " + volunteerId + " has already joined this challenge");
        }
        participants.add(volunteerId);
        c.setParticipantIds(participants);
        return toResponse(challengeRepository.save(c));
    }

    @Override
    public ChallengeResponse leaveChallenge(Long challengeId, Long volunteerId) {
        Challenge c = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + challengeId));
        List<Long> participants = c.getParticipantIds();
        if (participants == null || !participants.contains(volunteerId)) {
            throw new RuntimeException("Volunteer " + volunteerId + " is not part of this challenge");
        }
        participants.remove(volunteerId);
        c.setParticipantIds(participants);
        return toResponse(challengeRepository.save(c));
    }

    @Override
    public void deleteChallenge(Long id) {
        if (!challengeRepository.existsById(id)) {
            throw new RuntimeException("Challenge not found with id: " + id);
        }
        challengeRepository.deleteById(id);
    }
}
