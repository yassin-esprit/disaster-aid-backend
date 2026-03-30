package tn.esprit.disasteraidbackend.modules.communication.Mappers;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

public class NotificationMapper {

    public static Notification toEntity(NotificationRequestDTO dto, User recipient) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setBody(dto.getBody());
        notification.setData(dto.getData());
        notification.setChannel(dto.getChannel());
        notification.setIsRead(dto.getIsRead());
        notification.setIsCritical(dto.getIsCritical());
        notification.setSentAt(dto.getSentAt());
        notification.setReadAt(dto.getReadAt());
        return notification;
    }

    public static NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setNotificationId(notification.getNotificationId());

        if (notification.getRecipient() != null) {
            dto.setRecipientId(notification.getRecipient().getUserId());
            dto.setRecipientEmail(notification.getRecipient().getEmail());
        }

        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setBody(notification.getBody());
        dto.setData(notification.getData());
        dto.setChannel(notification.getChannel());
        dto.setIsRead(notification.getIsRead());
        dto.setIsCritical(notification.getIsCritical());
        dto.setSentAt(notification.getSentAt());
        dto.setReadAt(notification.getReadAt());
        return dto;
    }

    public static void updateEntityFromDTO(Notification notification, NotificationRequestDTO dto, User recipient) {
        notification.setRecipient(recipient);
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setBody(dto.getBody());
        notification.setData(dto.getData());
        notification.setChannel(dto.getChannel());
        notification.setIsRead(dto.getIsRead());
        notification.setIsCritical(dto.getIsCritical());
        notification.setSentAt(dto.getSentAt());
        notification.setReadAt(dto.getReadAt());
    }
}