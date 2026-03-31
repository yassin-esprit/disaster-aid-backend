package tn.esprit.disasteraidbackend.modules.reviews.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.EntityType;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;
import tn.esprit.disasteraidbackend.modules.reviews.entity.TrustScore;
import tn.esprit.disasteraidbackend.modules.reviews.repository.ReviewRepository;
import tn.esprit.disasteraidbackend.modules.reviews.repository.TrustScoreRepository;

@Service
public class TrustScoreServiceImpl implements ITrustScoreService {

    private final TrustScoreRepository trustScoreRepository;
    private final ReviewRepository reviewRepository;

    public TrustScoreServiceImpl(TrustScoreRepository trustScoreRepository, ReviewRepository reviewRepository) {
        this.trustScoreRepository = trustScoreRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public TrustScore addTrustScore(TrustScoreCreateDto dto) {
        TrustScore ts = new TrustScore();
        ts.setEntityId(dto.getEntityId());
        ts.setEntityType(dto.getEntityType());
        ts.setScore(dto.getScore());
        ts.setReviewCount(dto.getReviewCount());
        ts.setAverageRating(dto.getAverageRating());
        ts.setCompletionRate(dto.getCompletionRate());
        ts.setResponseRate(dto.getResponseRate());
        return trustScoreRepository.save(ts);
    }

    @Override
    public TrustScore getTrustScore(Integer id) {
        return trustScoreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TrustScore not found: " + id));
    }

    @Override
    public TrustScore getTrustScoreByEntity(Integer entityId, EntityType entityType) {
        return trustScoreRepository.findByEntityIdAndEntityType(entityId, entityType)
                .orElseThrow(() -> new EntityNotFoundException(
                        "TrustScore not found for entity " + entityId + " (" + entityType + ")"));
    }

    @Override
    public TrustScore updateTrustScore(Integer id, TrustScoreUpdateDto dto) {
        TrustScore ts = getTrustScore(id);
        ts.setEntityId(dto.getEntityId());
        ts.setEntityType(dto.getEntityType());
        ts.setScore(dto.getScore());
        ts.setReviewCount(dto.getReviewCount());
        ts.setAverageRating(dto.getAverageRating());
        ts.setCompletionRate(dto.getCompletionRate());
        ts.setResponseRate(dto.getResponseRate());
        return trustScoreRepository.save(ts);
    }

    @Override
    public void deleteTrustScore(Integer id) {
        trustScoreRepository.deleteById(id);
    }

    @Override
    public List<TrustScore> getAllTrustScores() {
        return trustScoreRepository.findAll();
    }

    @Override
    public List<TrustScore> getTrustScoresByEntityType(EntityType entityType) {
        return trustScoreRepository.findByEntityType(entityType);
    }

    @Override
    public TrustScore recalculate(Integer entityId, EntityType entityType) {
        ReviewTarget target = entityType == EntityType.VOLUNTEER
                ? ReviewTarget.VOLUNTEER : ReviewTarget.ORGANISATION;

        List<Review> reviews = reviewRepository.findByTargetIdAndTargetType(entityId, target)
                .stream()
                .filter(r -> r.getStatus() == ReviewStatus.PUBLISHED)
                .toList();

        int count = reviews.size();
        double avgRating = count == 0 ? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        // Score formula: average rating scaled to 0-100, weighted by review count
        double score = count == 0 ? 0.0 : Math.min(100.0, (avgRating / 5.0) * 100.0);

        TrustScore ts = trustScoreRepository.findByEntityIdAndEntityType(entityId, entityType)
                .orElseGet(() -> {
                    TrustScore fresh = new TrustScore();
                    fresh.setEntityId(entityId);
                    fresh.setEntityType(entityType);
                    return fresh;
                });

        ts.setReviewCount(count);
        ts.setAverageRating(avgRating);
        ts.setScore(score);
        return trustScoreRepository.save(ts);
    }
}
