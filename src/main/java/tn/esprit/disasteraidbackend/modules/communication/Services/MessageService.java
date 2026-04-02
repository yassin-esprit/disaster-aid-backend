package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.MessageMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.MessageRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;
import tn.esprit.disasteraidbackend.modules.identity.Services.SecurityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public MessageService(MessageRepository messageRepository,
                          ConversationRepository conversationRepository,
                          UserRepository userRepository,
                          SecurityService securityService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> retrieveAllMessages() {
        User currentUser = securityService.getCurrentUser();

        if (securityService.isAdmin(currentUser)) {
            return messageRepository.findAll()
                    .stream()
                    .map(MessageMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return messageRepository.findAll()
                .stream()
                .filter(message ->
                        message.getConversation() != null
                                && message.getConversation().getParticipantIds() != null
                                && message.getConversation().getParticipantIds().contains(currentUser.getUserId()))
                .map(MessageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageResponseDTO retrieveMessage(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!securityService.isAdmin(currentUser)
                && (message.getConversation() == null
                || message.getConversation().getParticipantIds() == null
                || !message.getConversation().getParticipantIds().contains(currentUser.getUserId()))) {
            throw new RuntimeException("Access denied");
        }

        return MessageMapper.toResponseDTO(message);
    }

    public MessageResponseDTO addMessage(MessageRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        if (!securityService.isAdmin(currentUser)) {
            if (!sender.getUserId().equals(currentUser.getUserId())) {
                throw new RuntimeException("Access denied: you can only send messages as yourself");
            }

            if (conversation.getParticipantIds() == null
                    || !conversation.getParticipantIds().contains(currentUser.getUserId())) {
                throw new RuntimeException("Access denied: you are not a participant in this conversation");
            }
        }

        Message message = MessageMapper.toEntity(dto, conversation, sender);
        Message savedMessage = messageRepository.save(message);

        return MessageMapper.toResponseDTO(savedMessage);
    }

    public MessageResponseDTO updateMessage(Integer id, MessageRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!securityService.isAdmin(currentUser)
                && !existingMessage.getSender().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied: you can only update your own messages");
        }

        Conversation conversation = existingMessage.getConversation();
        User sender = existingMessage.getSender();

        if (securityService.isAdmin(currentUser)) {
            conversation = conversationRepository.findById(dto.getConversationId())
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));

            sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender user not found"));
        } else {
            if (existingMessage.getConversation() == null
                    || existingMessage.getConversation().getParticipantIds() == null
                    || !existingMessage.getConversation().getParticipantIds().contains(currentUser.getUserId())) {
                throw new RuntimeException("Access denied");
            }
        }

        MessageMapper.updateEntityFromDTO(existingMessage, dto, conversation, sender);

        // Force original sender/conversation for normal users
        if (!securityService.isAdmin(currentUser)) {
            existingMessage.setConversation(conversation);
            existingMessage.setSender(sender);
        }

        existingMessage.setEditedAt(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(existingMessage);
        return MessageMapper.toResponseDTO(updatedMessage);
    }

    public void removeMessage(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!securityService.isAdmin(currentUser)
                && !message.getSender().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied: you can only delete your own messages");
        }

        if (!securityService.isAdmin(currentUser)
                && (message.getConversation() == null
                || message.getConversation().getParticipantIds() == null
                || !message.getConversation().getParticipantIds().contains(currentUser.getUserId()))) {
            throw new RuntimeException("Access denied");
        }

        messageRepository.delete(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMyMessages() {
        User currentUser = securityService.getCurrentUser();

        return messageRepository.findAll()
                .stream()
                .filter(message ->
                        message.getSender() != null
                                && message.getSender().getUserId().equals(currentUser.getUserId()))
                .map(MessageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}