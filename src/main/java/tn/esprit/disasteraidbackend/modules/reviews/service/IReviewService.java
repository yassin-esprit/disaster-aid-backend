package tn.esprit.disasteraidbackend.modules.reviews.service;

import java.util.List;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;

public interface IReviewService {
    Review addReview(ReviewCreateDto dto);
    Review getReview(Integer id);
    Review updateReview(Integer id, ReviewUpdateDto dto);
    void deleteReview(Integer id);
    List<Review> getAllReviews();
    List<Review> getReviewsByTarget(Integer targetId, ReviewTarget targetType);
    List<Review> getReviewsByAuthor(Integer authorId);
    List<Review> getReviewsByStatus(ReviewStatus status);
    Review markHelpful(Integer reviewId);
    Review moderateReview(Integer reviewId, ReviewStatus status);
    Review verifyParticipant(Integer reviewId);
}
