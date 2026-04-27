package tn.esprit.disasteraidbackend.analytics.m13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.ModelType;
import tn.esprit.disasteraidbackend.analytics.m13.entity.PredictiveModel;

import java.util.List;

public interface PredictiveModelRepository extends JpaRepository<PredictiveModel, Integer> {

    List<PredictiveModel> findByTypeOrderByTrainedAtDesc(ModelType type);

    List<PredictiveModel> findByIsActiveTrueOrderByTrainedAtDesc();

    long countByIsActiveTrue();
}

