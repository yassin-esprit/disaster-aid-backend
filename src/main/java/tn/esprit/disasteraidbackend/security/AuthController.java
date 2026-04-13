package tn.esprit.disasteraidbackend.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * POST /api/auth/login
 *
 * Accepts { "username": "...", "password": "...", "role": "ADMIN|OPERATOR|VIEWER" }
 * Returns  { "token": "...", "role": "...", "username": "..." }
 *
 * NOTE: This is a simplified implementation for the project scope.
 * In production, you would validate credentials against a User table
 * and use BCrypt for password hashing.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
        private String role; // ADMIN | OPERATOR | VIEWER
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // ── Hardcoded demo users (replace with DB lookup + BCrypt in production) ──
        boolean valid = switch (req.getUsername()) {
            case "admin"    -> "admin123".equals(req.getPassword());
            case "operator" -> "op123".equals(req.getPassword());
            case "viewer"   -> "view123".equals(req.getPassword());
            default         -> false;
        };

        if (!valid) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials"));
        }

        String role  = req.getRole() != null ? req.getRole().toUpperCase() : "VIEWER";
        String token = jwtUtil.generateToken(req.getUsername(), role);

        return ResponseEntity.ok(Map.of(
                "token",    token,
                "role",     role,
                "username", req.getUsername()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(jakarta.servlet.http.HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "No token"));
        }
        String token = auth.substring(7);
        if (!jwtUtil.isValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        return ResponseEntity.ok(Map.of(
                "username", jwtUtil.extractUsername(token),
                "role",     jwtUtil.extractRole(token)
        ));
    }
}