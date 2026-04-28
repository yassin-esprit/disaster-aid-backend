package tn.esprit.disasteraidbackend.modules.skills.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.skills.entity.Skill;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findByCategory(String category);
    List<Skill> findByIsActiveTrue();
    List<Skill> findByParentSkillId(Integer parentSkillId);
    Optional<Skill> findBySlug(String slug);
}
