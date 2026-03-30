package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}