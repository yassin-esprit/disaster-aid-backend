package tn.esprit.disasteraidbackend.modules.communication.Mappers;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;

public class ConversationMapper {

    public static Conversation toEntity(ConversationRequestDTO dto) {
        Conversation conversation = new Conversation();
        conversation.setType(dto.getType());
        conversation.setRelatedEntityId(dto.getRelatedEntityId());
        conversation.setRelatedEntityType(dto.getRelatedEntityType());
        conversation.setParticipantIds(dto.getParticipantIds());
        conversation.setLastMessageAt(dto.getLastMessageAt());
        return conversation;
    }

    public static ConversationResponseDTO toResponseDTO(Conversation conversation) {
        ConversationResponseDTO dto = new ConversationResponseDTO();
        dto.setConversationId(conversation.getConversationId());
        dto.setType(conversation.getType());
        dto.setRelatedEntityId(conversation.getRelatedEntityId());
        dto.setRelatedEntityType(conversation.getRelatedEntityType());
        dto.setParticipantIds(conversation.getParticipantIds());
        dto.setLastMessageAt(conversation.getLastMessageAt());
        dto.setCreatedAt(conversation.getCreatedAt());
        return dto;
    }

    public static void updateEntityFromDTO(Conversation conversation, ConversationRequestDTO dto) {
        conversation.setType(dto.getType());
        conversation.setRelatedEntityId(dto.getRelatedEntityId());
        conversation.setRelatedEntityType(dto.getRelatedEntityType());
        conversation.setParticipantIds(dto.getParticipantIds());
        conversation.setLastMessageAt(dto.getLastMessageAt());
    }
}