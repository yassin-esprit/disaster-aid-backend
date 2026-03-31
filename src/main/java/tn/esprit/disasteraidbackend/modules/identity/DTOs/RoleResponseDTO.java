package tn.esprit.disasteraidbackend.modules.identity.DTOs;

import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;

public class RoleResponseDTO {

    private Integer roleId;
    private Integer userId;
    private UserRole roleType;

    public RoleResponseDTO() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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