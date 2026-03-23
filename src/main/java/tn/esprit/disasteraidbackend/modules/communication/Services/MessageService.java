package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> retrieveAllMessages() {
        return messageRepository.findAll();
    }

    public Message retrieveMessage(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Message message) {
        Message existing = messageRepository.findById(message.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        existing.setBody(message.getBody());
        existing.setAttachments(message.getAttachments());
        existing.setEditedAt(message.getEditedAt());
        existing.setIsDeleted(message.getIsDeleted());

        return messageRepository.save(existing);
    }

    public void removeMessage(Integer id) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        messageRepository.delete(m);
    }
}