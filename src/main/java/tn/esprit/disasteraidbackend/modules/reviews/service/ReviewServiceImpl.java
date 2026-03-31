package tn.esprit.disasteraidbackend.modules.reviews.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;
import tn.esprit.disasteraidbackend.modules.reviews.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review addReview(ReviewCreateDto dto) {
        Review r = new Review();
        r.setAuthorId(dto.getAuthorId());
        r.setTargetId(dto.getTargetId());
        r.setTargetType(dto.getTargetType());
        r.setRating(dto.getRating());
        r.setBody(dto.getBody());
        r.setOpportunityId(dto.getOpportunityId());
        return reviewRepository.save(r);
    }

    @Override
    public Review getReview(Integer id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
    }

    @Override
    public Review updateReview(Integer id, ReviewUpdateDto dto) {
        Review r = getReview(id);
        r.setAuthorId(dto.getAuthorId());
        r.setTargetId(dto.getTargetId());
        r.setTargetType(dto.getTargetType());
        r.setRating(dto.getRating());
        r.setBody(dto.getBody());
        r.setOpportunityId(dto.getOpportunityId());
        r.setStatus(dto.getStatus());
        if (dto.getIsVerifiedParticipant() != null) {
            r.setIsVerifiedParticipant(dto.getIsVerifiedParticipant());
        }
        return reviewRepository.save(r);
    }

    @Override
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByTarget(Integer targetId, ReviewTarget targetType) {
        return reviewRepository.findByTargetIdAndTargetType(targetId, targetType);
    }

    @Override
    public List<Review> getReviewsByAuthor(Integer authorId) {
        return reviewRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Review> getReviewsByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status);
    }

    @Override
    public Review markHelpful(Integer reviewId) {
        Review r = getReview(reviewId);
        r.setHelpfulVotes(r.getHelpfulVotes() + 1);
        return reviewRepository.save(r);
    }

    @Override
    public Review moderateReview(Integer reviewId, ReviewStatus status) {
        Review r = getReview(reviewId);
        r.setStatus(status);
        return reviewRepository.save(r);
    }

    @Override
    public Review verifyParticipant(Integer reviewId) {
        Review r = getReview(reviewId);
        r.setIsVerifiedParticipant(true);
        return reviewRepository.save(r);
    }
}
