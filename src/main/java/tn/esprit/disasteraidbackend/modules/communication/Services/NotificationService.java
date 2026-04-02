package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.NotificationMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;
import tn.esprit.disasteraidbackend.modules.identity.Services.SecurityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               SecurityService securityService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> retrieveAllNotifications() {
        User currentUser = securityService.getCurrentUser();

        if (securityService.isAdmin(currentUser)) {
            return notificationRepository.findAll()
                    .stream()
                    .map(NotificationMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return notificationRepository.findByRecipient_UserId(currentUser.getUserId())
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationResponseDTO retrieveNotification(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!securityService.isAdmin(currentUser)
                && !notification.getRecipient().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        return NotificationMapper.toResponseDTO(notification);
    }

    public NotificationResponseDTO addNotification(NotificationRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser)) {
            throw new RuntimeException("Access denied: only admins can create notifications");
        }

        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient user not found"));

        Notification notification = NotificationMapper.toEntity(dto, recipient);
        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.toResponseDTO(savedNotification);
    }

    public NotificationResponseDTO updateNotification(Integer id, NotificationRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!securityService.isAdmin(currentUser)
                && !existingNotification.getRecipient().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        User recipient = existingNotification.getRecipient();

        // Normal users cannot change notification owner
        if (securityService.isAdmin(currentUser)) {
            recipient = userRepository.findById(dto.getRecipientId())
                    .orElseThrow(() -> new RuntimeException("Recipient user not found"));
        }

        NotificationMapper.updateEntityFromDTO(existingNotification, dto, recipient);

        // Keep owner unchanged for non-admin users
        if (!securityService.isAdmin(currentUser)) {
            existingNotification.setRecipient(recipient);
        }

        // Auto-manage readAt when read status changes
        if (Boolean.TRUE.equals(existingNotification.getIsRead()) && existingNotification.getReadAt() == null) {
            existingNotification.setReadAt(LocalDateTime.now());
        }

        if (Boolean.FALSE.equals(existingNotification.getIsRead())) {
            existingNotification.setReadAt(null);
        }

        Notification updatedNotification = notificationRepository.save(existingNotification);
        return NotificationMapper.toResponseDTO(updatedNotification);
    }

    public void removeNotification(Integer id) {
        User currentUser = securityService.getCurrentUser();

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!securityService.isAdmin(currentUser)
                && !notification.getRecipient().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        notificationRepository.delete(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getMyNotifications() {
        User currentUser = securityService.getCurrentUser();

        return notificationRepository.findByRecipient_UserId(currentUser.getUserId())
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}