package tn.esprit.disasteraidbackend.modules.reviews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationAction;

public interface ModerationActionRepository extends JpaRepository<ModerationAction, Integer> {
    List<ModerationAction> findByTargetIdAndTargetType(Integer targetId, String targetType);
    List<ModerationAction> findByAdminId(Integer adminId);
}
