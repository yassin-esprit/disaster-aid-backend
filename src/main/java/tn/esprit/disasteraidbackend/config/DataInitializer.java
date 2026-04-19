package tn.esprit.disasteraidbackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.UserRepository;

import java.util.List;

/**
 * Initialise des données de base au démarrage (dev/test).
 * Idempotent: ne fait rien si la table users contient déjà des lignes.
 */
@Component
@ConditionalOnProperty(name = "app.init-data.enabled", havingValue = "true", matchIfMissing = false)
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.saveAll(List.of(
                createUser("Alice"),
                createUser("Bob"),
                createUser("Charlie")
        ));
    }

    private static User createUser(String name) {
        User u = new User();
        u.setName(name);
        return u;
    }
}

