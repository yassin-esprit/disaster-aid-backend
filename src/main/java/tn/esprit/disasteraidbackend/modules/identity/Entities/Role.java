package tn.esprit.disasteraidbackend.modules.identity.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 32)
    private UserRole roleType;

    public Role() {
    }

    public Role(Integer roleId, User user, UserRole roleType) {
        this.roleId = roleId;
        this.user = user;
        this.roleType = roleType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRole getRoleType() {
        return roleType;
    }

    public void setRoleType(UserRole roleType) {
        this.roleType = roleType;
    }
}