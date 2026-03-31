package tn.esprit.disasteraidbackend.modules.identity.DTOs;

import jakarta.validation.constraints.NotNull;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;

public class RoleRequestDTO {

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotNull(message = "Role type is required")
    private UserRole roleType;

    public RoleRequestDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserRole getRoleType() {
        return roleType;
    }

    public void setRoleType(UserRole roleType) {
        this.roleType = roleType;
    }
}