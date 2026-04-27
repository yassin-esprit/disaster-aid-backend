package tn.esprit.disasteraidbackend.modules.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByTargetIdAndTargetType(Integer targetId, ReviewTarget targetType);
    List<Review> findByAuthorId(Integer authorId);
    List<Review> findByStatus(ReviewStatus status);
}
