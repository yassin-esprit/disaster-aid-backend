package tn.esprit.disasteraidbackend.modules.identity.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    CITIZEN,
    VOLUNTEER,
    RESPONDER,
    ORG_ADMIN,
    AGENCY_ADMIN,
    PLATFORM_ADMIN;

    @JsonCreator
    public static UserRole fromJson(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        switch (normalized) {
            case "ORGANISATION":
            case "ORGANIZATION":
            case "ORG":
            case "ORG_ADMIN":
                return ORG_ADMIN;
            case "AGENCY":
            case "AGENCY_ADMIN":
                return AGENCY_ADMIN;
            case "PLATFORM":
            case "PLATFORM_ADMIN":
                return PLATFORM_ADMIN;
            default:
                return UserRole.valueOf(normalized);
        }
    }
}
