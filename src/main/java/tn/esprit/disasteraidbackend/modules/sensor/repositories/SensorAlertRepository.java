package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorAlertRepository extends JpaRepository<Alert, Integer> {

    List<Alert> findByStatus(Alert.AlertStatus status);
    List<Alert> findByDisasterType(Alert.DisasterType disasterType);
    List<Alert> findBySeverity(Alert.Severity severity);
    List<Alert> findByIssuedAtAfter(LocalDateTime issuedAt);

    Page<Alert> findAll(Pageable pageable);
    Page<Alert> findByStatus(Alert.AlertStatus status, Pageable pageable);
    Page<Alert> findBySeverity(Alert.Severity severity, Pageable pageable);
    Page<Alert> findByDisasterType(Alert.DisasterType type, Pageable pageable);

    @Query("SELECT a FROM SensorWarning a WHERE a.status = 'MONITORING' AND a.issuedAt < :cutoff")
    List<Alert> findMonitoringAlertsBefore(@Param("cutoff") LocalDateTime cutoff);

    @Query(value = "SELECT a1.* FROM alerts a1 JOIN alerts a2 ON a1.id <> a2.id " +
            "WHERE a1.id = :alertId " +
            "AND a1.affected_area IS NOT NULL " +
            "AND a2.affected_area IS NOT NULL " +
            "AND ST_Intersects(a1.affected_area, a2.affected_area) = true " +
            "AND NOT ST_Equals(a1.affected_area, a2.affected_area)",
            nativeQuery = true)
    List<Alert> findAlertsOverlappingWith(@Param("alertId") Integer alertId);
}
