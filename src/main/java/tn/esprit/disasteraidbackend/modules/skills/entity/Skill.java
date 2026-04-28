package tn.esprit.disasteraidbackend.modules.skills.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String category;

    @Column(name = "parent_skill_id")
    private Integer parentSkillId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_verifiable", nullable = false)
    private Boolean isVerifiable;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
