package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> retrieveAllConversations() {
        return conversationRepository.findAll();
    }

    public Conversation retrieveConversation(Integer id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    public Conversation addConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public Conversation updateConversation(Conversation conversation) {
        Conversation existing = conversationRepository.findById(conversation.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        existing.setType(conversation.getType());
        existing.setRelatedEntityId(conversation.getRelatedEntityId());
        existing.setRelatedEntityType(conversation.getRelatedEntityType());
        existing.setParticipantIds(conversation.getParticipantIds());
        existing.setLastMessageAt(conversation.getLastMessageAt());

        return conversationRepository.save(existing);
    }

    public void removeConversation(Integer id) {
        Conversation c = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversationRepository.delete(c);
    }
}