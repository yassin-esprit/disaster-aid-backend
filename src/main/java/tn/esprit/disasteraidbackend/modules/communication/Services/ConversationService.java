package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.ConversationMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Services.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final SecurityService securityService;

    public ConversationService(ConversationRepository conversationRepository,
                               SecurityService securityService) {
        this.conversationRepository = conversationRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> retrieveAllConversations() {
        User currentUser = securityService.getCurrentUser();

        if (securityService.isAdmin(currentUser)) {
            return conversationRepository.findAll()
                    .stream()
                    .map(ConversationMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return conversationRepository.findAll()
                .stream()
                .filter(conversation ->
                        conversation.getParticipantIds() != null &&
                                conversation.getParticipantIds().contains(currentUser.getUserId()))
                .map(ConversationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationResponseDTO retrieveConversation(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!securityService.isAdmin(currentUser)
                && (conversation.getParticipantIds() == null
                || !conversation.getParticipantIds().contains(currentUser.getUserId()))) {
            throw new RuntimeException("Access denied");
        }

        return ConversationMapper.toResponseDTO(conversation);
    }

    public ConversationResponseDTO addConversation(ConversationRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser)) {
            if (dto.getParticipantIds() == null || !dto.getParticipantIds().contains(currentUser.getUserId())) {
                throw new RuntimeException("Access denied: you can only create a conversation that includes you");
            }
        }

        Conversation conversation = ConversationMapper.toEntity(dto);
        Conversation savedConversation = conversationRepository.save(conversation);

        return ConversationMapper.toResponseDTO(savedConversation);
    }

    public ConversationResponseDTO updateConversation(Integer id, ConversationRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        Conversation existingConversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!securityService.isAdmin(currentUser)
                && (existingConversation.getParticipantIds() == null
                || !existingConversation.getParticipantIds().contains(currentUser.getUserId()))) {
            throw new RuntimeException("Access denied");
        }

        if (!securityService.isAdmin(currentUser)) {
            if (dto.getParticipantIds() == null || !dto.getParticipantIds().contains(currentUser.getUserId())) {
                throw new RuntimeException("Access denied: you cannot remove yourself from the conversation");
            }
        }

        ConversationMapper.updateEntityFromDTO(existingConversation, dto);
        Conversation updatedConversation = conversationRepository.save(existingConversation);

        return ConversationMapper.toResponseDTO(updatedConversation);
    }

    public void removeConversation(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!securityService.isAdmin(currentUser)
                && (conversation.getParticipantIds() == null
                || !conversation.getParticipantIds().contains(currentUser.getUserId()))) {
            throw new RuntimeException("Access denied");
        }

        conversationRepository.delete(conversation);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getMyConversations() {
        User currentUser = securityService.getCurrentUser();

        return conversationRepository.findAll()
                .stream()
                .filter(conversation ->
                        conversation.getParticipantIds() != null &&
                                conversation.getParticipantIds().contains(currentUser.getUserId()))
                .map(ConversationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}