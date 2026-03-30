package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationPreferenceService;

import java.util.List;

@RestController
@RequestMapping("/communication/preferences")
@CrossOrigin("*")
@Tag(name = "Notification Preference", description = "Gestion des préférences de notification")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    public NotificationPreferenceController(NotificationPreferenceService notificationPreferenceService) {
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @Operation(summary = "Récupérer toutes les préférences de notification")
    @GetMapping("/retrieve-all")
    public List<NotificationPreferenceResponseDTO> getAll() {
        return notificationPreferenceService.retrieveAllPreferences();
    }

    @Operation(summary = "Récupérer une préférence par ID")
    @GetMapping("/{id}")
    public NotificationPreferenceResponseDTO getById(
            @Parameter(description = "ID de la préférence")
            @PathVariable Integer id) {
        return notificationPreferenceService.retrievePreference(id);
    }

    @Operation(summary = "Ajouter une préférence de notification")
    @PostMapping("/add")
    public NotificationPreferenceResponseDTO add(@RequestBody NotificationPreferenceRequestDTO dto) {
        return notificationPreferenceService.addPreference(dto);
    }

    @Operation(summary = "Mettre à jour une préférence de notification")
    @PutMapping("/update/{id}")
    public NotificationPreferenceResponseDTO update(
            @Parameter(description = "ID de la préférence à modifier")
            @PathVariable Integer id,
            @RequestBody NotificationPreferenceRequestDTO dto) {
        return notificationPreferenceService.updatePreference(id, dto);
    }

    @Operation(summary = "Supprimer une préférence de notification")
    @DeleteMapping("/delete/{id}")
    public void delete(
            @Parameter(description = "ID de la préférence à supprimer")
            @PathVariable Integer id) {
        notificationPreferenceService.removePreference(id);
    }
}