package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("""
            select distinct c
            from Conversation c
            left join fetch c.participantIds p
            where c.conversationId = :conversationId
            """)
    Optional<Conversation> findByIdWithParticipants(@Param("conversationId") Integer conversationId);
}