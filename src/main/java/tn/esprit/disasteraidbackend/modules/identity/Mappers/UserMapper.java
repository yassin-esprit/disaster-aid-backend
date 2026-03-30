package tn.esprit.disasteraidbackend.modules.identity.Mappers;

import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPasswordHash());
        user.setRole(dto.getRole());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setLocation(dto.getLocation());
        user.setProfilePhoto(dto.getProfilePhoto());
        user.setEmailVerified(dto.getEmailVerified());
        user.setPhoneVerified(dto.getPhoneVerified());
        user.setMfaEnabled(dto.getMfaEnabled());
        user.setStatus(dto.getStatus());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setLocation(user.getLocation());
        dto.setProfilePhoto(user.getProfilePhoto());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setPhoneVerified(user.getPhoneVerified());
        dto.setMfaEnabled(user.getMfaEnabled());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLogin(user.getLastLogin());
        return dto;
    }

    public static void updateEntityFromDTO(User user, UserRequestDTO dto) {
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPasswordHash());
        user.setRole(dto.getRole());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setLocation(dto.getLocation());
        user.setProfilePhoto(dto.getProfilePhoto());
        user.setEmailVerified(dto.getEmailVerified());
        user.setPhoneVerified(dto.getPhoneVerified());
        user.setMfaEnabled(dto.getMfaEnabled());
        user.setStatus(dto.getStatus());
    }
}