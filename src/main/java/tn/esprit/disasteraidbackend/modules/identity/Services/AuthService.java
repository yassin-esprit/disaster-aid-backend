package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.AuthResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.LoginRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.Entities.AccountStatus;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;
import tn.esprit.disasteraidbackend.modules.identity.Mappers.UserMapper;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public AuthResponseDTO register(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Mapper DTO → Entity
        User user = UserMapper.toEntity(request);

        // Password
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // ✅ Correction ici : ne pas forcer CITIZEN
        user.setRole(
                request.getRole() != null ? request.getRole() : UserRole.CITIZEN
        );

        // Status par défaut
        user.setStatus(
                request.getStatus() != null ? request.getStatus() : AccountStatus.ACTIVE
        );

        // Sécurité (valeurs par défaut)
        if (user.getEmailVerified() == null) user.setEmailVerified(false);
        if (user.getPhoneVerified() == null) user.setPhoneVerified(false);
        if (user.getMfaEnabled() == null) user.setMfaEnabled(false);

        // Save
        User savedUser = userRepository.save(user);

        // Génération JWT
        String jwtToken = jwtService.generateToken(savedUser);

        // Response
        return new AuthResponseDTO(
                jwtToken,
                "Bearer",
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "User registered successfully"
        );
    }
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return new AuthResponseDTO(
                jwtToken,
                "Bearer",
                user.getUserId(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful"
        );
    }

    public String logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }

        String token = authHeader.substring(7);
        Date expirationDate = jwtService.extractExpiration(token);
        tokenBlacklistService.blacklistToken(token, expirationDate);

        return "Logout successful";
    }
}