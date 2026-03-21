package tn.esprit.disasteraidbackend.User.IdentityAccess.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserRole role;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 20)
    private String phone;

    private String location;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "is_phone_verified")
    private Boolean isPhoneVerified;

    @Column(name = "is_mfa_enabled")
    private Boolean isMfaEnabled;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AccountStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public Boolean getPhoneVerified() {
        return isPhoneVerified;
    }

    public Boolean getMfaEnabled() {
        return isMfaEnabled;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.isEmailVerified = emailVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.isPhoneVerified = phoneVerified;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.isMfaEnabled = mfaEnabled;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (role == null) {
            role = UserRole.CITIZEN;
        }
        if (status == null) {
            status = AccountStatus.ACTIVE;
        }
        if (isEmailVerified == null) {
            isEmailVerified = false;
        }
        if (isPhoneVerified == null) {
            isPhoneVerified = false;
        }
        if (isMfaEnabled == null) {
            isMfaEnabled = false;
        }
    }
}