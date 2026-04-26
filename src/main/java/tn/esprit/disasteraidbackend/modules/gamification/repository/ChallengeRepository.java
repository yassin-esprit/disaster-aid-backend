package tn.esprit.disasteraidbackend.modules.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Challenge;
import tn.esprit.disasteraidbackend.modules.gamification.entity.ChallengeStatus;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByStatus(ChallengeStatus status);

    // Find all challenges a specific volunteer has joined
    @Query("SELECT c FROM Challenge c WHERE :volunteerId MEMBER OF c.participantIds")
    List<Challenge> findByParticipantId(@Param("volunteerId") Long volunteerId);

    List<Challenge> findByStatusIn(List<ChallengeStatus> statuses);
}
