package tn.esprit.disasteraidbackend.modules.identity.DTOs;

import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;

public class RoleRequestDTO {

    private Integer userId;
    private UserRole roleType;

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