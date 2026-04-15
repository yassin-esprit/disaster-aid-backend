package tn.esprit.disasteraidbackend.modules.identity.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tn.esprit.disasteraidbackend.modules.identity.Entities.AccountStatus;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;
import tn.esprit.disasteraidbackend.modules.identity.Services.JwtService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.oauth2.redirect-uri}")
    private String frontendRedirectUri;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        String providerId = extractProviderId(oAuth2User);
        String extractedEmail = extractEmail(registrationId, oAuth2User);
        String firstName = extractFirstName(registrationId, oAuth2User);
        String lastName = extractLastName(registrationId, oAuth2User);
        String picture = extractPicture(registrationId, oAuth2User);

        if ((extractedEmail == null || extractedEmail.isBlank()) && "facebook".equalsIgnoreCase(registrationId)) {
            extractedEmail = "facebook_" + providerId + "@placeholder.local";
        }

        if (extractedEmail == null || extractedEmail.isBlank()) {
            response.sendRedirect(frontendRedirectUri + "?error=email_not_found");
            return;
        }

        final String email = extractedEmail;
        final String finalFirstName = firstName;
        final String finalLastName = lastName;
        final String finalPicture = picture;

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(finalFirstName != null && !finalFirstName.isBlank() ? finalFirstName : "Social");
            newUser.setLastName(finalLastName != null && !finalLastName.isBlank() ? finalLastName : "User");
            newUser.setProfilePhoto(finalPicture);
            newUser.setPasswordHash(UUID.randomUUID().toString());
            newUser.setRole(UserRole.CITIZEN);
            newUser.setStatus(AccountStatus.ACTIVE);
            newUser.setEmailVerified(true);
            newUser.setPhoneVerified(false);
            newUser.setMfaEnabled(false);
            newUser.setCreatedAt(LocalDateTime.now());
            return newUser;
        });

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName);
        }
        if (picture != null && !picture.isBlank()) {
            user.setProfilePhoto(picture);
        }

        user.setEmailVerified(true);
        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);

        String redirectUrl = frontendRedirectUri
                + "?token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8)
                + "&userId=" + savedUser.getUserId()
                + "&email=" + URLEncoder.encode(savedUser.getEmail(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(savedUser.getRole().name(), StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }

    private String extractProviderId(OAuth2User user) {
        Object id = user.getAttribute("id");
        return id != null ? id.toString() : UUID.randomUUID().toString();
    }

    private String extractEmail(String provider, OAuth2User user) {
        return user.getAttribute("email");
    }

    private String extractFirstName(String provider, OAuth2User user) {
        if ("google".equalsIgnoreCase(provider)) {
            return user.getAttribute("given_name");
        }
        if ("facebook".equalsIgnoreCase(provider)) {
            String first = user.getAttribute("first_name");
            if (first != null) return first;

            String name = user.getAttribute("name");
            if (name != null && !name.isBlank()) {
                String[] parts = name.trim().split("\\s+");
                return parts[0];
            }
        }
        return null;
    }

    private String extractLastName(String provider, OAuth2User user) {
        if ("google".equalsIgnoreCase(provider)) {
            return user.getAttribute("family_name");
        }
        if ("facebook".equalsIgnoreCase(provider)) {
            String last = user.getAttribute("last_name");
            if (last != null) return last;

            String name = user.getAttribute("name");
            if (name != null && !name.isBlank()) {
                String[] parts = name.trim().split("\\s+");
                return parts.length > 1 ? parts[parts.length - 1] : "";
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String extractPicture(String provider, OAuth2User user) {
        if ("google".equalsIgnoreCase(provider)) {
            return user.getAttribute("picture");
        }

        if ("facebook".equalsIgnoreCase(provider)) {
            Object picObj = user.getAttribute("picture");
            if (picObj instanceof Map<?, ?> pictureMap) {
                Object dataObj = pictureMap.get("data");
                if (dataObj instanceof Map<?, ?> dataMap) {
                    Object urlObj = dataMap.get("url");
                    if (urlObj != null) {
                        return urlObj.toString();
                    }
                }
            }
        }

        return null;
    }
}