package tn.esprit.disasteraidbackend.modules.communication.Mappers;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

public class MessageMapper {

    public static Message toEntity(MessageRequestDTO dto, Conversation conversation, User sender) {
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setBody(dto.getBody());
        message.setAttachments(dto.getAttachments());
        message.setSentAt(dto.getSentAt());
        message.setEditedAt(dto.getEditedAt());
        message.setDeleted(dto.getIsDeleted());
        return message;
    }

    public static MessageResponseDTO toResponseDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();

        dto.setMessageId(message.getMessageId());

        if (message.getConversation() != null) {
            dto.setConversationId(message.getConversation().getConversationId());
        }

        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getUserId());
            dto.setSenderEmail(message.getSender().getEmail());
        }

        dto.setBody(message.getBody());
        dto.setAttachments(message.getAttachments());
        dto.setSentAt(message.getSentAt());
        dto.setEditedAt(message.getEditedAt());
        dto.setIsDeleted(message.getDeleted());

        return dto;
    }

    public static void updateEntityFromDTO(Message message, MessageRequestDTO dto,
                                           Conversation conversation, User sender) {
        message.setConversation(conversation);
        message.setSender(sender);
        message.setBody(dto.getBody());
        message.setAttachments(dto.getAttachments());
        message.setSentAt(dto.getSentAt());
        message.setEditedAt(dto.getEditedAt());
        message.setDeleted(dto.getIsDeleted());
    }
}