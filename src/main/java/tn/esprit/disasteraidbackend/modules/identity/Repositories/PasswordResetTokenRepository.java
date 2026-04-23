package tn.esprit.disasteraidbackend.modules.identity.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByEmailAndCodeAndUsedFalse(String email, String code);
    void deleteByEmail(String email);
}
