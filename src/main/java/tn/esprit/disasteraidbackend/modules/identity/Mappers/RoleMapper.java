package tn.esprit.disasteraidbackend.modules.identity.Mappers;

import tn.esprit.disasteraidbackend.modules.identity.DTOs.RoleRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.RoleResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Entities.Role;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

public class RoleMapper {

    private RoleMapper() {
    }

    public static Role toEntity(RoleRequestDTO dto, User user) {
        Role role = new Role();
        role.setUser(user);
        role.setRoleType(dto.getRoleType());
        return role;
    }

    public static RoleResponseDTO toResponseDTO(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setRoleId(role.getRoleId());
        dto.setUserId(role.getUser().getUserId());
        dto.setRoleType(role.getRoleType());
        return dto;
    }
}