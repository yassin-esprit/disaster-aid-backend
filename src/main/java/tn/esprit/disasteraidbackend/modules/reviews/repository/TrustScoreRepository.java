package tn.esprit.disasteraidbackend.modules.reviews.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.reviews.entity.EntityType;
import tn.esprit.disasteraidbackend.modules.reviews.entity.TrustScore;

public interface TrustScoreRepository extends JpaRepository<TrustScore, Integer> {
    Optional<TrustScore> findByEntityIdAndEntityType(Integer entityId, EntityType entityType);
    List<TrustScore> findByEntityType(EntityType entityType);
}
