package tn.esprit.disasteraidbackend.modules.reviews.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.TrustScoreUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.EntityType;
import tn.esprit.disasteraidbackend.modules.reviews.entity.TrustScore;
import tn.esprit.disasteraidbackend.modules.reviews.service.ITrustScoreService;

import java.util.List;

@RestController
@RequestMapping("/reviews/trust-scores")
public class TrustScoreController {

    private final ITrustScoreService trustScoreService;

    public TrustScoreController(ITrustScoreService trustScoreService) {
        this.trustScoreService = trustScoreService;
    }

    @PostMapping
    public TrustScore add(@Valid @RequestBody TrustScoreCreateDto dto) {
        return trustScoreService.addTrustScore(dto);
    }

    @GetMapping("/{id}")
    public TrustScore get(@PathVariable Integer id) {
        return trustScoreService.getTrustScore(id);
    }

    @GetMapping
    public List<TrustScore> getAll() {
        return trustScoreService.getAllTrustScores();
    }

    @GetMapping("/by-entity")
    public TrustScore getByEntity(@RequestParam Integer entityId,
                                  @RequestParam EntityType entityType) {
        return trustScoreService.getTrustScoreByEntity(entityId, entityType);
    }

    @GetMapping("/by-type")
    public List<TrustScore> getByEntityType(@RequestParam EntityType entityType) {
        return trustScoreService.getTrustScoresByEntityType(entityType);
    }

    @PutMapping("/{id}")
    public TrustScore update(@PathVariable Integer id, @Valid @RequestBody TrustScoreUpdateDto dto) {
        return trustScoreService.updateTrustScore(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        trustScoreService.deleteTrustScore(id);
    }

    @PostMapping("/recalculate")
    public TrustScore recalculate(@RequestParam Integer entityId,
                                  @RequestParam EntityType entityType) {
        return trustScoreService.recalculate(entityId, entityType);
    }
}
