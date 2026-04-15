package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.ConversationRequest;
import tn.esprit.disasteraidbackend.modules.communication.Entities.ConversationRequestStatus;

import java.util.List;

public interface ConversationRequestRepository extends JpaRepository<ConversationRequest, Integer> {
    List<ConversationRequest> findByCitizenId(Integer citizenId);
    List<ConversationRequest> findByAdminId(Integer adminId);
    List<ConversationRequest> findByStatus(ConversationRequestStatus status);
}