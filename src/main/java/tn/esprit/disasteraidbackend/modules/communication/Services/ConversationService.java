package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.ConversationMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<ConversationResponseDTO> retrieveAllConversations() {
        return conversationRepository.findAll()
                .stream()
                .map(ConversationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ConversationResponseDTO retrieveConversation(Integer id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        return ConversationMapper.toResponseDTO(conversation);
    }

    public ConversationResponseDTO addConversation(ConversationRequestDTO dto) {
        Conversation conversation = ConversationMapper.toEntity(dto);
        Conversation savedConversation = conversationRepository.save(conversation);

        return ConversationMapper.toResponseDTO(savedConversation);
    }

    public ConversationResponseDTO updateConversation(Integer id, ConversationRequestDTO dto) {
        Conversation existingConversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        ConversationMapper.updateEntityFromDTO(existingConversation, dto);
        Conversation updatedConversation = conversationRepository.save(existingConversation);

        return ConversationMapper.toResponseDTO(updatedConversation);
    }

    public void removeConversation(Integer id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversationRepository.delete(conversation);
    }
}