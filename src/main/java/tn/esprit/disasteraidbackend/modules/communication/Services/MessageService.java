package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.MessageMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.MessageRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          ConversationRepository conversationRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public List<MessageResponseDTO> retrieveAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(MessageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MessageResponseDTO retrieveMessage(Integer id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        return MessageMapper.toResponseDTO(message);
    }

    public MessageResponseDTO addMessage(MessageRequestDTO dto) {
        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        Message message = MessageMapper.toEntity(dto, conversation, sender);
        Message savedMessage = messageRepository.save(message);

        return MessageMapper.toResponseDTO(savedMessage);
    }

    public MessageResponseDTO updateMessage(Integer id, MessageRequestDTO dto) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        MessageMapper.updateEntityFromDTO(existingMessage, dto, conversation, sender);
        Message updatedMessage = messageRepository.save(existingMessage);

        return MessageMapper.toResponseDTO(updatedMessage);
    }

    public void removeMessage(Integer id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        messageRepository.delete(message);
    }
}