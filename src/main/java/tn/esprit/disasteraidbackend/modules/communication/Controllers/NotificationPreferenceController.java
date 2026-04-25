package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationPreferenceService;

import java.util.List;

@RestController
@RequestMapping("/api/communication/preferences")
@CrossOrigin("*")
@Tag(name = "Notification Preference", description = "Notification preference management")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    public NotificationPreferenceController(NotificationPreferenceService notificationPreferenceService) {
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @Operation(summary = "Retrieve notification preferences")
    @GetMapping("/retrieve-all")
    public ResponseEntity<List<NotificationPreferenceResponseDTO>> getAll() {
        return ResponseEntity.ok(notificationPreferenceService.retrieveAllPreferences());
    }

    @Operation(summary = "Retrieve my notification preferences")
    @GetMapping("/me")
    public ResponseEntity<List<NotificationPreferenceResponseDTO>> getMyPreferences() {
        return ResponseEntity.ok(notificationPreferenceService.getMyPreferences());
    }

    @Operation(summary = "Retrieve a notification preference by ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationPreferenceResponseDTO> getById(
            @Parameter(description = "Preference ID")
            @PathVariable Integer id) {
        return ResponseEntity.ok(notificationPreferenceService.retrievePreference(id));
    }

    @Operation(summary = "Create a notification preference")
    @PostMapping("/add")
    public ResponseEntity<NotificationPreferenceResponseDTO> add(
            @RequestBody NotificationPreferenceRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationPreferenceService.addPreference(dto));
    }

    @Operation(summary = "Update a notification preference")
    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationPreferenceResponseDTO> update(
            @Parameter(description = "Preference ID to update")
            @PathVariable Integer id,
            @RequestBody NotificationPreferenceRequestDTO dto) {
        return ResponseEntity.ok(notificationPreferenceService.updatePreference(id, dto));
    }

    @Operation(summary = "Delete a notification preference")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Preference ID to delete")
            @PathVariable Integer id) {
        notificationPreferenceService.removePreference(id);
        return ResponseEntity.noContent().build();
    }
}