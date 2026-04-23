package tn.esprit.disasteraidbackend.modules.identity.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.ForgotPasswordRequest;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.ResetPasswordRequest;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.VerifyCodeRequest;
import tn.esprit.disasteraidbackend.modules.identity.Services.PasswordResetService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }
    // constructeur...

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgot(@RequestBody ForgotPasswordRequest req) {
        service.sendResetCode(req.getEmail());
        return ResponseEntity.ok("Reset code sent.");
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verify(@RequestBody VerifyCodeRequest req) {
        service.verifyCode(req.getEmail(), req.getCode());
        return ResponseEntity.ok("Code verified.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> reset(@RequestBody ResetPasswordRequest req) {
        service.resetPassword(req.getEmail(), req.getCode(), req.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }
}