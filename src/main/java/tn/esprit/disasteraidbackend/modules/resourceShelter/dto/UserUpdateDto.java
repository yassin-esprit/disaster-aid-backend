package tn.esprit.disasteraidbackend.modules.resourceShelter.dto;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateDto {
    @NotBlank
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}