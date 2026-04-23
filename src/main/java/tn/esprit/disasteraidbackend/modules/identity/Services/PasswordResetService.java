package tn.esprit.disasteraidbackend.modules.identity.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.identity.Entities.PasswordResetToken;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.PasswordResetTokenRepository;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
public class PasswordResetService {

    @Value("${app.reset-code.expiry-minutes:10}")
    private int expiryMinutes;

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepo,
            UserRepository userRepo,
            PasswordEncoder encoder,
            JavaMailSender mailSender) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.mailSender = mailSender;
    }

    public void sendResetCode(String email) {
        if (!userRepo.existsByEmail(email))
            throw new RuntimeException("No account found with this email.");

        tokenRepo.deleteByEmail(email);

        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        tokenRepo.save(token);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Your password reset code");
        msg.setText("Your verification code is: " + code + "\nValid for " + expiryMinutes + " minutes.");
        mailSender.send(msg);
    }

    public void verifyCode(String email, String code) {
        PasswordResetToken token = tokenRepo
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired code."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Code expired.");
    }

    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetToken token = tokenRepo
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired code."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Code expired.");

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setPasswordHash(encoder.encode(newPassword));
        userRepo.save(user);

        token.setUsed(true);
        tokenRepo.save(token);
    }
}