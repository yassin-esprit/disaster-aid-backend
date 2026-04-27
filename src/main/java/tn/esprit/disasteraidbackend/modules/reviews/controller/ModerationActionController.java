package tn.esprit.disasteraidbackend.modules.reviews.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationAction;
import tn.esprit.disasteraidbackend.modules.reviews.service.IModerationActionService;

import java.util.List;

@RestController
@RequestMapping("/reviews/moderation")
public class ModerationActionController {

    private final IModerationActionService moderationActionService;

    public ModerationActionController(IModerationActionService moderationActionService) {
        this.moderationActionService = moderationActionService;
    }

    @PostMapping
    public ModerationAction add(@Valid @RequestBody ModerationActionCreateDto dto) {
        return moderationActionService.addAction(dto);
    }

    @GetMapping("/{id}")
    public ModerationAction get(@PathVariable Integer id) {
        return moderationActionService.getAction(id);
    }

    @GetMapping
    public List<ModerationAction> getAll() {
        return moderationActionService.getAllActions();
    }

    @PutMapping("/{id}")
    public ModerationAction update(@PathVariable Integer id,
                                   @Valid @RequestBody ModerationActionUpdateDto dto) {
        return moderationActionService.updateAction(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moderationActionService.deleteAction(id);
    }

    @GetMapping("/by-target/{targetId}")
    public List<ModerationAction> getByTarget(@PathVariable Integer targetId,
                                              @RequestParam String targetType) {
        return moderationActionService.getActionsByTarget(targetId, targetType);
    }

    @GetMapping("/by-admin/{adminId}")
    public List<ModerationAction> getByAdmin(@PathVariable Integer adminId) {
        return moderationActionService.getActionsByAdmin(adminId);
    }
}
