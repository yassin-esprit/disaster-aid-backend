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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO register(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // Secure defaults for public registration
        user.setRole(UserRole.CITIZEN);
        user.setStatus(AccountStatus.ACTIVE);

        if (user.getEmailVerified() == null) {
            user.setEmailVerified(false);
        }
        if (user.getPhoneVerified() == null) {
            user.setPhoneVerified(false);
        }
        if (user.getMfaEnabled() == null) {
            user.setMfaEnabled(false);
        }

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);

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

    public String logout() {
        return "Logout successful. Please remove the token on the client side.";
    }
}