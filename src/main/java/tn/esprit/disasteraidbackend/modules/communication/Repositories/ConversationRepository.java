package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}