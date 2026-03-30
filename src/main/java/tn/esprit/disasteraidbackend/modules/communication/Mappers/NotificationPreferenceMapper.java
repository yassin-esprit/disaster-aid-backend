package tn.esprit.disasteraidbackend.modules.communication.Mappers;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

public class NotificationPreferenceMapper {

    public static NotificationPreference toEntity(NotificationPreferenceRequestDTO dto, User user) {
        NotificationPreference preference = new NotificationPreference();
        preference.setUser(user);
        preference.setType(dto.getType());
        preference.setChannels(dto.getChannels());
        preference.setEnabled(dto.getEnabled());
        preference.setQuietHoursFrom(dto.getQuietHoursFrom());
        preference.setQuietHoursTo(dto.getQuietHoursTo());
        return preference;
    }

    public static NotificationPreferenceResponseDTO toResponseDTO(NotificationPreference preference) {
        NotificationPreferenceResponseDTO dto = new NotificationPreferenceResponseDTO();
        dto.setPrefId(preference.getPrefId());

        if (preference.getUser() != null) {
            dto.setUserId(preference.getUser().getUserId());
            dto.setUserEmail(preference.getUser().getEmail());
        }

        dto.setType(preference.getType());
        dto.setChannels(preference.getChannels());
        dto.setEnabled(preference.getEnabled());
        dto.setQuietHoursFrom(preference.getQuietHoursFrom());
        dto.setQuietHoursTo(preference.getQuietHoursTo());

        return dto;
    }

    public static void updateEntityFromDTO(NotificationPreference preference,
                                           NotificationPreferenceRequestDTO dto,
                                           User user) {
        preference.setUser(user);
        preference.setType(dto.getType());
        preference.setChannels(dto.getChannels());
        preference.setEnabled(dto.getEnabled());
        preference.setQuietHoursFrom(dto.getQuietHoursFrom());
        preference.setQuietHoursTo(dto.getQuietHoursTo());
    }
}