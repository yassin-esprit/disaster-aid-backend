package tn.esprit.disasteraidbackend.modules.reviews.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionCreateDto;
import tn.esprit.disasteraidbackend.modules.reviews.dto.ModerationActionUpdateDto;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationAction;
import tn.esprit.disasteraidbackend.modules.reviews.repository.ModerationActionRepository;

@Service
public class ModerationActionServiceImpl implements IModerationActionService {

    private final ModerationActionRepository moderationActionRepository;

    public ModerationActionServiceImpl(ModerationActionRepository moderationActionRepository) {
        this.moderationActionRepository = moderationActionRepository;
    }

    @Override
    public ModerationAction addAction(ModerationActionCreateDto dto) {
        ModerationAction a = new ModerationAction();
        a.setAdminId(dto.getAdminId());
        a.setTargetType(dto.getTargetType());
        a.setTargetId(dto.getTargetId());
        a.setAction(dto.getAction());
        a.setReason(dto.getReason());
        return moderationActionRepository.save(a);
    }

    @Override
    public ModerationAction getAction(Integer id) {
        return moderationActionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ModerationAction not found: " + id));
    }

    @Override
    public ModerationAction updateAction(Integer id, ModerationActionUpdateDto dto) {
        ModerationAction a = getAction(id);
        a.setAdminId(dto.getAdminId());
        a.setTargetType(dto.getTargetType());
        a.setTargetId(dto.getTargetId());
        a.setAction(dto.getAction());
        a.setReason(dto.getReason());
        return moderationActionRepository.save(a);
    }

    @Override
    public void deleteAction(Integer id) {
        moderationActionRepository.deleteById(id);
    }

    @Override
    public List<ModerationAction> getAllActions() {
        return moderationActionRepository.findAll();
    }

    @Override
    public List<ModerationAction> getActionsByTarget(Integer targetId, String targetType) {
        return moderationActionRepository.findByTargetIdAndTargetType(targetId, targetType);
    }

    @Override
    public List<ModerationAction> getActionsByAdmin(Integer adminId) {
        return moderationActionRepository.findByAdminId(adminId);
    }
}
