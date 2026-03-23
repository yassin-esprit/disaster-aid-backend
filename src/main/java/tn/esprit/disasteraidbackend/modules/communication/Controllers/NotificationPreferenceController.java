package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
import tn.esprit.disasteraidbackend.modules.communication.Services.NotificationPreferenceService;

import java.util.List;

@RestController
@RequestMapping("/communication/preferences")
@CrossOrigin("*")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    public NotificationPreferenceController(NotificationPreferenceService notificationPreferenceService) {
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @GetMapping("/retrieve-all")
    public List<NotificationPreference> getAll() {
        return notificationPreferenceService.retrieveAllPreferences();
    }

    @GetMapping("/{id}")
    public NotificationPreference getById(@PathVariable Integer id) {
        return notificationPreferenceService.retrievePreference(id);
    }

    @PostMapping("/add")
    public NotificationPreference add(@RequestBody NotificationPreference preference) {
        return notificationPreferenceService.addPreference(preference);
    }

    @PutMapping("/update")
    public NotificationPreference update(@RequestBody NotificationPreference preference) {
        return notificationPreferenceService.updatePreference(preference);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        notificationPreferenceService.removePreference(id);
    }
}