package tn.esprit.disasteraidbackend.User.IdentityAccess.Services;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.User.IdentityAccess.Entities.User;
import tn.esprit.disasteraidbackend.User.IdentityAccess.Repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public User retrieveUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setPasswordHash(user.getPasswordHash());
        existingUser.setRole(user.getRole());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhone(user.getPhone());
        existingUser.setLocation(user.getLocation());
        existingUser.setProfilePhoto(user.getProfilePhoto());
        existingUser.setEmailVerified(user.getEmailVerified());
        existingUser.setPhoneVerified(user.getPhoneVerified());
        existingUser.setMfaEnabled(user.getMfaEnabled());
        existingUser.setStatus(user.getStatus());
        existingUser.setLastLogin(user.getLastLogin());

        return userRepository.save(existingUser);
    }

    public void removeUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}