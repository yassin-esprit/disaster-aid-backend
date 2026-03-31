package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
    List<Alert> findByStatus(Alert.AlertStatus status);
    List<Alert> findByDisasterType(Alert.DisasterType disasterType);
    List<Alert> findBySeverity(Alert.Severity severity);
    List<Alert> findByIssuedAtAfter(LocalDateTime issuedAt);
}