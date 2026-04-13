package tn.esprit.disasteraidbackend.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Alert, Integer> {

    @Query("SELECT a.disasterType, COUNT(a) FROM Alert a " +
            "WHERE (:from IS NULL OR a.issuedAt >= :from) " +
            "AND   (:to   IS NULL OR a.issuedAt <= :to) " +
            "GROUP BY a.disasterType")
    List<Object[]> countByDisasterType(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to);

    @Query("SELECT a.status, COUNT(a) FROM Alert a GROUP BY a.status")
    List<Object[]> countByStatus();

    @Query("SELECT a.severity, COUNT(a) FROM Alert a GROUP BY a.severity")
    List<Object[]> countBySeverity();

    @Query("SELECT sd.sensor.id, AVG(sd.value) FROM SensorData sd " +
            "WHERE sd.recordedAt >= :since GROUP BY sd.sensor.id")
    List<Object[]> avgReadingPerSensorSince(@Param("since") LocalDateTime since);

    @Query("SELECT sd.sensor.id, " +
            "  SUM(CASE WHEN sd.isAnomaly = true THEN 1 ELSE 0 END) * 100.0 / COUNT(sd) " +
            "FROM SensorData sd GROUP BY sd.sensor.id")
    List<Object[]> anomalyRatePerSensor();

    @Query("SELECT er.status, COUNT(er) FROM EvacuationRoute er GROUP BY er.status")
    List<Object[]> routeCountByStatus();

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.issuedAt >= :since")
    long countAlertsAfter(@Param("since") LocalDateTime since);

    @Query("SELECT s.status, COUNT(s) FROM Sensor s GROUP BY s.status")
    List<Object[]> sensorCountByStatus();
}
