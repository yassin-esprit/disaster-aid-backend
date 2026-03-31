package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Mappers.UserMapper;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> retrieveAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO retrieveUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        return UserMapper.toResponseDTO(user);
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        if (!existingUser.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserMapper.updateEntityFromDTO(existingUser, dto);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponseDTO(updatedUser);
    }

    public void removeUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        userRepository.delete(user);
    }
}