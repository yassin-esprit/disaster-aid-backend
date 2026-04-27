package tn.esprit.disasteraidbackend.modules.reviews.service;

import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationAction;

import java.util.List;

public interface IModerationActionService {
    ModerationAction addAction(ModerationActionCreateDto dto);
    ModerationAction getAction(Integer id);
    ModerationAction updateAction(Integer id, ModerationActionUpdateDto dto);
    void deleteAction(Integer id);
    List<ModerationAction> getAllActions();
    List<ModerationAction> getActionsByTarget(Integer targetId, String targetType);
    List<ModerationAction> getActionsByAdmin(Integer adminId);
}
