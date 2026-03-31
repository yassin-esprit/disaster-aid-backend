package tn.esprit.disasteraidbackend.modules.reviews.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ReviewUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.Review;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;
import tn.esprit.disasteraidbackend.modules.reviews.service.IReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review add(@Valid @RequestBody ReviewCreateDto dto) {
        return reviewService.addReview(dto);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable Integer id) {
        return reviewService.getReview(id);
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAllReviews();
    }

    @PutMapping("/{id}")
    public Review update(@PathVariable Integer id, @Valid @RequestBody ReviewUpdateDto dto) {
        return reviewService.updateReview(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/by-target/{targetId}")
    public List<Review> getByTarget(@PathVariable Integer targetId,
                                    @RequestParam ReviewTarget targetType) {
        return reviewService.getReviewsByTarget(targetId, targetType);
    }

    @GetMapping("/by-author/{authorId}")
    public List<Review> getByAuthor(@PathVariable Integer authorId) {
        return reviewService.getReviewsByAuthor(authorId);
    }

    @GetMapping("/by-status")
    public List<Review> getByStatus(@RequestParam ReviewStatus status) {
        return reviewService.getReviewsByStatus(status);
    }

    @PostMapping("/{id}/helpful")
    public Review markHelpful(@PathVariable Integer id) {
        return reviewService.markHelpful(id);
    }

    @PatchMapping("/{id}/moderate")
    public Review moderate(@PathVariable Integer id, @RequestParam ReviewStatus status) {
        return reviewService.moderateReview(id, status);
    }

    @PatchMapping("/{id}/verify-participant")
    public Review verifyParticipant(@PathVariable Integer id) {
        return reviewService.verifyParticipant(id);
    }
}
