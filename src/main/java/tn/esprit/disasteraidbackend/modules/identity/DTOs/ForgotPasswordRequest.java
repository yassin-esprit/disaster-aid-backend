package tn.esprit.disasteraidbackend.modules.identity.DTOs;

public class ForgotPasswordRequest {
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
