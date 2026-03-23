package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> retrieveAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification retrieveNotification(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Notification notification) {
        Notification existingNotification = notificationRepository.findById(notification.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        existingNotification.setRecipient(notification.getRecipient());
        existingNotification.setType(notification.getType());
        existingNotification.setTitle(notification.getTitle());
        existingNotification.setBody(notification.getBody());
        existingNotification.setData(notification.getData());
        existingNotification.setChannel(notification.getChannel());
        existingNotification.setIsRead(notification.getIsRead());
        existingNotification.setIsCritical(notification.getIsCritical());
        existingNotification.setSentAt(notification.getSentAt());
        existingNotification.setReadAt(notification.getReadAt());

        return notificationRepository.save(existingNotification);
    }

    public void removeNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }
}