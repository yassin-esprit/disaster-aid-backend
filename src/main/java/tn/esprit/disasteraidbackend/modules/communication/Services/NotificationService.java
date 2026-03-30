package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.NotificationMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationResponseDTO> retrieveAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO retrieveNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        return NotificationMapper.toResponseDTO(notification);
    }

    public NotificationResponseDTO addNotification(NotificationRequestDTO dto) {
        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient user not found"));

        Notification notification = NotificationMapper.toEntity(dto, recipient);
        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.toResponseDTO(savedNotification);
    }

    public NotificationResponseDTO updateNotification(Integer id, NotificationRequestDTO dto) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient user not found"));

        NotificationMapper.updateEntityFromDTO(existingNotification, dto, recipient);
        Notification updatedNotification = notificationRepository.save(existingNotification);

        return NotificationMapper.toResponseDTO(updatedNotification);
    }

    public void removeNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }
}