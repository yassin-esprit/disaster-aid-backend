package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Date> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklistToken(String token, Date expirationDate) {
        if (token != null && expirationDate != null) {
            blacklistedTokens.put(token, expirationDate);
        }
    }

    public boolean isBlacklisted(String token) {
        cleanupExpiredTokens();
        return blacklistedTokens.containsKey(token);
    }

    private void cleanupExpiredTokens() {
        Date now = new Date();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}