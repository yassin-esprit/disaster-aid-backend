package tn.esprit.disasteraidbackend.modules.reviews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByTargetIdAndTargetType(Integer targetId, ReviewTarget targetType);
    List<Review> findByAuthorId(Integer authorId);
    List<Review> findByStatus(ReviewStatus status);
}
