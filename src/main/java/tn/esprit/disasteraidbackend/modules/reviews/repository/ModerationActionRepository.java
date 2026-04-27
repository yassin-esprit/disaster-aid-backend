package tn.esprit.disasteraidbackend.modules.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationAction;

import java.util.List;

public interface ModerationActionRepository extends JpaRepository<ModerationAction, Integer> {
    List<ModerationAction> findByTargetIdAndTargetType(Integer targetId, String targetType);
    List<ModerationAction> findByAdminId(Integer adminId);
}
