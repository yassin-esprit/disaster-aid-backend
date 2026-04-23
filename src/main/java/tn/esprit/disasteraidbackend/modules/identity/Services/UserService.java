package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.MessageRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationPreferenceRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationRepository;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UpdateMyProfileDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;
import tn.esprit.disasteraidbackend.modules.identity.Mappers.UserMapper;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final SecurityService securityService;
    private final FileService fileService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       MessageRepository messageRepository,
                       NotificationRepository notificationRepository,
                       NotificationPreferenceRepository notificationPreferenceRepository,
                       SecurityService securityService, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageRepository = messageRepository;
        this.notificationRepository = notificationRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.securityService = securityService;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> retrieveAllUsers() {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser)) {
            throw new RuntimeException("Access denied: only admins can view all users");
        }

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO retrieveUser(Integer id) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser) && !currentUser.getUserId().equals(id)) {
            throw new RuntimeException("Access denied");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        return UserMapper.toResponseDTO(user);
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser)) {
            throw new RuntimeException("Access denied: only admins can create users");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser) && !currentUser.getUserId().equals(id)) {
            throw new RuntimeException("Access denied");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        if (!existingUser.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!securityService.isAdmin(currentUser)) {
            dto.setRole(existingUser.getRole());
            dto.setEmailVerified(existingUser.getEmailVerified());
            dto.setPhoneVerified(existingUser.getPhoneVerified());
            dto.setMfaEnabled(existingUser.getMfaEnabled());
            dto.setStatus(existingUser.getStatus());
        }

        UserMapper.updateEntityFromDTO(existingUser, dto);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponseDTO(updatedUser);
    }

    public void removeUser(Integer id) {
        User currentUser = securityService.getCurrentUser();

        if (!securityService.isAdmin(currentUser)) {
            throw new RuntimeException("Access denied: only admins can delete users");
        }

        if (currentUser.getUserId().equals(id)) {
            throw new RuntimeException("You cannot delete your own account");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        try {
            messageRepository.deleteBySender_UserId(id);
            notificationRepository.deleteByRecipient_UserId(id);
            notificationPreferenceRepository.deleteByUser_UserId(id);

            userRepository.delete(user);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete this user because it is linked to other data");
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMyProfile() {
        User currentUser = securityService.getCurrentUser();
        return UserMapper.toResponseDTO(currentUser);
    }

    public UserResponseDTO updateMyProfile(UpdateMyProfileDTO request, MultipartFile profilePhoto) {
        User currentUser = securityService.getCurrentUser();
        currentUser.setFirstName(request.getFirstName());
        currentUser.setLastName(request.getLastName());
        currentUser.setPhone(request.getPhone());
        currentUser.setLocation(request.getLocation());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            String imageUrl;
            try {
                imageUrl = fileService.uploadProfileImage(profilePhoto);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading profile photo: " + e.getMessage(), e);
            }
            currentUser.setProfilePhoto(imageUrl);
        }

        User savedUser = userRepository.save(currentUser);
        return UserMapper.toResponseDTO(savedUser);
    }

}