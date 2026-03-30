package tn.esprit.disasteraidbackend.modules.identity.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 32)
    private UserRole roleType;

    public UserRole getRoleType() {
        return roleType;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoleType(UserRole roleType) {
        this.roleType = roleType;
    }

    public User getUser() {
        return user;
    }

    public Integer getRoleId() {
        return roleId;
    }


}