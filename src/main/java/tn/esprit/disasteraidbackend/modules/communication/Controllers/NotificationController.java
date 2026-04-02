package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/communication/notifications")
@CrossOrigin("*")
@Tag(name = "Notification", description = "Notification management")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Retrieve notifications")
    @GetMapping("/retrieve-all-notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.retrieveAllNotifications());
    }

    @Operation(summary = "Retrieve my notifications")
    @GetMapping("/my-notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    @Operation(summary = "Retrieve a notification by ID")
    @GetMapping("/retrieve-notification/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(
            @Parameter(description = "Notification ID")
            @PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.retrieveNotification(id));
    }

    @Operation(summary = "Create a notification")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','ORG_ADMIN','AGENCY_ADMIN')")
    @PostMapping("/add-notification")
    public ResponseEntity<NotificationResponseDTO> addNotification(
            @RequestBody NotificationRequestDTO notificationRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.addNotification(notificationRequestDTO));
    }

    @Operation(summary = "Update a notification")
    @PutMapping("/update-notification/{id}")
    public ResponseEntity<NotificationResponseDTO> updateNotification(
            @Parameter(description = "Notification ID to update")
            @PathVariable Integer id,
            @RequestBody NotificationRequestDTO notificationRequestDTO) {
        return ResponseEntity.ok(notificationService.updateNotification(id, notificationRequestDTO));
    }

    @Operation(summary = "Delete a notification")
    @DeleteMapping("/remove-notification/{id}")
    public ResponseEntity<Void> removeNotification(
            @Parameter(description = "Notification ID to delete")
            @PathVariable Integer id) {
        notificationService.removeNotification(id);
        return ResponseEntity.noContent().build();
    }
}