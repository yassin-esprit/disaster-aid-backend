package tn.esprit.disasteraidbackend.analytics.m14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.analytics.m14.entity.ChurnPrediction;

import java.util.List;
import java.util.Optional;

public interface ChurnPredictionRepository extends JpaRepository<ChurnPrediction, Integer> {

    List<ChurnPrediction> findByPredictedChurnProbGreaterThanOrderByPredictedChurnProbDesc(Double threshold);

    Optional<ChurnPrediction> findTopByVolunteerIdOrderByPredictedAtDesc(Integer volunteerId);
}

