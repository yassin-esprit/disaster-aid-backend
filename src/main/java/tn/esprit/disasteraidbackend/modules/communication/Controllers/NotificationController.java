package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/communication/notifications")
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/retrieve-all-notifications")
    public List<Notification> getAllNotifications() {
        return notificationService.retrieveAllNotifications();
    }

    @GetMapping("/retrieve-notification/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationService.retrieveNotification(id);
    }

    @PostMapping("/add-notification")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationService.addNotification(notification);
    }

    @PutMapping("/update-notification")
    public Notification updateNotification(@RequestBody Notification notification) {
        return notificationService.updateNotification(notification);
    }

    @DeleteMapping("/remove-notification/{id}")
    public void removeNotification(@PathVariable Integer id) {
        notificationService.removeNotification(id);
    }
}