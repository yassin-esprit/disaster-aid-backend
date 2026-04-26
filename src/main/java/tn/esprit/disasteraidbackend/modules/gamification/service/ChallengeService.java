package tn.esprit.disasteraidbackend.modules.gamification.service;

import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.ChallengeStatus;

import java.util.List;

public interface ChallengeService {

    ChallengeResponse createChallenge(ChallengeRequest request);

    ChallengeResponse getChallengeById(Long id);

    List<ChallengeResponse> getAllChallenges();

    List<ChallengeResponse> getByStatus(ChallengeStatus status);

    List<ChallengeResponse> getChallengesByVolunteer(Long volunteerId);

    ChallengeResponse updateChallenge(Long id, ChallengeRequest request);

    ChallengeResponse joinChallenge(Long challengeId, Long volunteerId);

    ChallengeResponse leaveChallenge(Long challengeId, Long volunteerId);

    void deleteChallenge(Long id);
}
