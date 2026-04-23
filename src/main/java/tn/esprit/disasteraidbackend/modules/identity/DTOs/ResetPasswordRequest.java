package tn.esprit.disasteraidbackend.modules.identity.DTOs;

public class ResetPasswordRequest {
    private String email;
    private String code;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
