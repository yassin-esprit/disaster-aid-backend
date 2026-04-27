package tn.esprit.disasteraidbackend.modules.reviews.service;

import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.EntityType;
import tn.esprit.disasteraidbackend.modules.reviews.entity.TrustScore;

import java.util.List;

public interface ITrustScoreService {
    TrustScore addTrustScore(TrustScoreCreateDto dto);
    TrustScore getTrustScore(Integer id);
    TrustScore getTrustScoreByEntity(Integer entityId, EntityType entityType);
    TrustScore updateTrustScore(Integer id, TrustScoreUpdateDto dto);
    void deleteTrustScore(Integer id);
    List<TrustScore> getAllTrustScores();
    List<TrustScore> getTrustScoresByEntityType(EntityType entityType);
    TrustScore recalculate(Integer entityId, EntityType entityType);
}
