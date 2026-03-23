package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationPreferenceRepository;

import java.util.List;

@Service
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;

    public NotificationPreferenceService(NotificationPreferenceRepository notificationPreferenceRepository) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }

    public List<NotificationPreference> retrieveAllPreferences() {
        return notificationPreferenceRepository.findAll();
    }

    public NotificationPreference retrievePreference(Integer id) {
        return notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));
    }

    public NotificationPreference addPreference(NotificationPreference preference) {
        return notificationPreferenceRepository.save(preference);
    }

    public NotificationPreference updatePreference(NotificationPreference preference) {
        NotificationPreference existing = notificationPreferenceRepository.findById(preference.getPrefId())
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));

        existing.setUser(preference.getUser());
        existing.setType(preference.getType());
        existing.setChannels(preference.getChannels());
        existing.setIsEnabled(preference.getIsEnabled());
        existing.setQuietHoursFrom(preference.getQuietHoursFrom());
        existing.setQuietHoursTo(preference.getQuietHoursTo());

        return notificationPreferenceRepository.save(existing);
    }

    public void removePreference(Integer id) {
        NotificationPreference preference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));
        notificationPreferenceRepository.delete(preference);
    }
}