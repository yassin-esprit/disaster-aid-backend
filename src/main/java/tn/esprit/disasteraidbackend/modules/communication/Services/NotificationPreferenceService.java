package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.NotificationPreferenceMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationPreferenceRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final UserRepository userRepository;

    public NotificationPreferenceService(NotificationPreferenceRepository notificationPreferenceRepository,
                                         UserRepository userRepository) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationPreferenceResponseDTO> retrieveAllPreferences() {
        return notificationPreferenceRepository.findAll()
                .stream()
                .map(NotificationPreferenceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificationPreferenceResponseDTO retrievePreference(Integer id) {
        NotificationPreference preference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));

        return NotificationPreferenceMapper.toResponseDTO(preference);
    }

    public NotificationPreferenceResponseDTO addPreference(NotificationPreferenceRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationPreference preference = NotificationPreferenceMapper.toEntity(dto, user);
        NotificationPreference savedPreference = notificationPreferenceRepository.save(preference);

        return NotificationPreferenceMapper.toResponseDTO(savedPreference);
    }

    public NotificationPreferenceResponseDTO updatePreference(Integer id, NotificationPreferenceRequestDTO dto) {
        NotificationPreference existingPreference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationPreferenceMapper.updateEntityFromDTO(existingPreference, dto, user);
        NotificationPreference updatedPreference = notificationPreferenceRepository.save(existingPreference);

        return NotificationPreferenceMapper.toResponseDTO(updatedPreference);
    }

    public void removePreference(Integer id) {
        NotificationPreference preference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NotificationPreference not found"));
        notificationPreferenceRepository.delete(preference);
    }
}