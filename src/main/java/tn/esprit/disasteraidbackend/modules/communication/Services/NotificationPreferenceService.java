package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.NotificationPreferenceResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
import tn.esprit.disasteraidbackend.modules.communication.Mappers.NotificationPreferenceMapper;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationPreferenceRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;
import tn.esprit.disasteraidbackend.modules.identity.Services.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public NotificationPreferenceService(NotificationPreferenceRepository notificationPreferenceRepository,
                                         UserRepository userRepository,
                                         SecurityService securityService) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public List<NotificationPreferenceResponseDTO> retrieveAllPreferences() {
        User currentUser = securityService.getCurrentUser();

        if (securityService.isAdmin(currentUser)) {
            return notificationPreferenceRepository.findAll()
                    .stream()
                    .map(NotificationPreferenceMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return notificationPreferenceRepository.findByUser_UserId(currentUser.getUserId())
                .stream()
                .map(NotificationPreferenceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationPreferenceResponseDTO retrievePreference(Integer id) {
        User currentUser = securityService.getCurrentUser();

        NotificationPreference preference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification preference not found"));

        if (!securityService.isAdmin(currentUser)
                && !preference.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        return NotificationPreferenceMapper.toResponseDTO(preference);
    }

    public NotificationPreferenceResponseDTO addPreference(NotificationPreferenceRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        User targetUser = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!securityService.isAdmin(currentUser)
                && !targetUser.getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        NotificationPreference preference = NotificationPreferenceMapper.toEntity(dto, targetUser);
        NotificationPreference savedPreference = notificationPreferenceRepository.save(preference);

        return NotificationPreferenceMapper.toResponseDTO(savedPreference);
    }

    public NotificationPreferenceResponseDTO updatePreference(Integer id, NotificationPreferenceRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        NotificationPreference existingPreference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification preference not found"));

        if (!securityService.isAdmin(currentUser)
                && !existingPreference.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        User targetUser = existingPreference.getUser();

        // Only admins can reassign the preference to another user
        if (securityService.isAdmin(currentUser)) {
            targetUser = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        NotificationPreferenceMapper.updateEntityFromDTO(existingPreference, dto, targetUser);
        existingPreference.setUser(targetUser);

        NotificationPreference updatedPreference = notificationPreferenceRepository.save(existingPreference);
        return NotificationPreferenceMapper.toResponseDTO(updatedPreference);
    }

    public void removePreference(Integer id) {
        User currentUser = securityService.getCurrentUser();

        NotificationPreference preference = notificationPreferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification preference not found"));

        if (!securityService.isAdmin(currentUser)
                && !preference.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        notificationPreferenceRepository.delete(preference);
    }

    @Transactional(readOnly = true)
    public List<NotificationPreferenceResponseDTO> getMyPreferences() {
        User currentUser = securityService.getCurrentUser();

        return notificationPreferenceRepository.findByUser_UserId(currentUser.getUserId())
                .stream()
                .map(NotificationPreferenceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}