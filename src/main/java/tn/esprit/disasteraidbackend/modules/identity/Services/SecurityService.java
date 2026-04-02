package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authenticated user found");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public boolean isAdmin(User user) {
        return user.getRole() == UserRole.PLATFORM_ADMIN
                || user.getRole() == UserRole.ORG_ADMIN
                || user.getRole() == UserRole.AGENCY_ADMIN;
    }

    public boolean isCurrentUser(Integer userId) {
        User currentUser = getCurrentUser();
        return currentUser.getUserId().equals(userId);
    }

    public boolean isCurrentUserOrAdmin(Integer userId) {
        User currentUser = getCurrentUser();
        return currentUser.getUserId().equals(userId) || isAdmin(currentUser);
    }
}