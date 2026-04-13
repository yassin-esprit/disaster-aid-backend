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
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    // ── Original queries ──────────────────────────────────────────────────────
    List<Alert> findByStatus(Alert.AlertStatus status);
    List<Alert> findByDisasterType(Alert.DisasterType disasterType);
    List<Alert> findBySeverity(Alert.Severity severity);
    List<Alert> findByIssuedAtAfter(LocalDateTime issuedAt);

    // ── Pagination + Sorting ─────────────────────────────────────────────────
    Page<Alert> findAll(Pageable pageable);
    Page<Alert> findByStatus(Alert.AlertStatus status, Pageable pageable);
    Page<Alert> findBySeverity(Alert.Severity severity, Pageable pageable);
    Page<Alert> findByDisasterType(Alert.DisasterType type, Pageable pageable);

    // ── Scheduling: MONITORING alerts older than N hours ─────────────────────
    @Query("SELECT a FROM Alert a WHERE a.status = 'MONITORING' AND a.issuedAt < :cutoff")
    List<Alert> findMonitoringAlertsBefore(@Param("cutoff") LocalDateTime cutoff);

    // ── Spatial: overlapping alert areas ─────────────────────────────────────
    @Query(value = "SELECT a1.* FROM alerts a1 JOIN alerts a2 ON a1.id <> a2.id " +
            "WHERE ST_Overlaps(a1.affected_area, a2.affected_area) = true " +
            "AND a1.id = :alertId",
            nativeQuery = true)
    List<Alert> findAlertsOverlappingWith(@Param("alertId") Integer alertId);
}