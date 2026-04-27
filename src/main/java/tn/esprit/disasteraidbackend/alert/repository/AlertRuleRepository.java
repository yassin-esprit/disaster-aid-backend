package tn.esprit.disasteraidbackend.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.alert.entity.AlertRule;

import java.util.List;

public interface AlertRuleRepository extends JpaRepository<AlertRule, Integer> {
    List<AlertRule> findByActiveTrueOrderByCreatedAtDesc();
}
