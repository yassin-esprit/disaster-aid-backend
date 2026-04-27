package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.entity.Alert;

@Service
public class AlertNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public AlertNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAlertCreated(Alert alert) {
        messagingTemplate.convertAndSend("/topic/alerts", alert);
    }

    public void notifyAlertUpdated(Alert alert) {
        messagingTemplate.convertAndSend("/topic/alerts", alert);
    }

    public void notifyAlertDeleted(Integer alertId) {
        messagingTemplate.convertAndSend("/topic/alerts/deleted", alertId);
    }
}
