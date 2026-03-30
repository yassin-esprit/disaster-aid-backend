package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/communication/notifications")
@CrossOrigin("*")
@Tag(name = "Notification", description = "Gestion des notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Récupérer toutes les notifications")
    @GetMapping("/retrieve-all-notifications")
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationService.retrieveAllNotifications();
    }

    @Operation(summary = "Récupérer une notification par ID")
    @GetMapping("/retrieve-notification/{id}")
    public NotificationResponseDTO getNotificationById(
            @Parameter(description = "ID de la notification")
            @PathVariable Integer id) {
        return notificationService.retrieveNotification(id);
    }

    @Operation(summary = "Ajouter une notification")
    @PostMapping("/add-notification")
    public NotificationResponseDTO addNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        return notificationService.addNotification(notificationRequestDTO);
    }

    @Operation(summary = "Mettre à jour une notification")
    @PutMapping("/update-notification/{id}")
    public NotificationResponseDTO updateNotification(
            @Parameter(description = "ID de la notification à modifier")
            @PathVariable Integer id,
            @RequestBody NotificationRequestDTO notificationRequestDTO) {
        return notificationService.updateNotification(id, notificationRequestDTO);
    }

    @Operation(summary = "Supprimer une notification")
    @DeleteMapping("/remove-notification/{id}")
    public void removeNotification(
            @Parameter(description = "ID de la notification à supprimer")
            @PathVariable Integer id) {
        notificationService.removeNotification(id);
    }
}