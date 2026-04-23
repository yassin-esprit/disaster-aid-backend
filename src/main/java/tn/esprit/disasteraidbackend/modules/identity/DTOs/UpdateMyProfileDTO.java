package tn.esprit.disasteraidbackend.modules.identity.DTOs;

import jakarta.validation.constraints.Size;

public class UpdateMyProfileDTO {

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    private String phone;

    private String location;

    private String profilePhoto;

    public String getProfilePhoto() {return profilePhoto;}
    public void setProfilePhoto(String profilePhoto) {this.profilePhoto = profilePhoto;}
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}