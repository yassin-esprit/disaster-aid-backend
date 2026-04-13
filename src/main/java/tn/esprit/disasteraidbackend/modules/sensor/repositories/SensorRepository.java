package tn.esprit.disasteraidbackend.modules.sensor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    // ── Original queries ──────────────────────────────────────────────────────
    List<Sensor> findByStatus(Sensor.SensorStatus status);
    List<Sensor> findByType(Sensor.SensorType type);
    List<Sensor> findByOwnerId(Integer ownerId);

    // ── Pagination support ────────────────────────────────────────────────────
    Page<Sensor> findAll(Pageable pageable);
    Page<Sensor> findByStatus(Sensor.SensorStatus status, Pageable pageable);
    Page<Sensor> findByType(Sensor.SensorType type, Pageable pageable);

    // ── Scheduling: sensors with no ping since a given time ──────────────────
    @Query("SELECT s FROM Sensor s WHERE s.status = 'ACTIVE' " +
            "AND (s.lastPingAt IS NULL OR s.lastPingAt < :since)")
    List<Sensor> findActiveSensorsWithNoPingSince(@Param("since") LocalDateTime since);

    // ── Spatial: sensors inside a given WKT polygon ──────────────────────────
    // Requires PostGIS + hibernate-spatial on the classpath.
    @Query(value = "SELECT * FROM sensors s " +
            "WHERE ST_Contains(ST_GeomFromText(:polygon, 4326), s.location) = true",
            nativeQuery = true)
    List<Sensor> findSensorsInsidePolygon(@Param("polygon") String polygonWkt);

    // ── Spatial: nearest sensor to a given WKT point ────────────────────────
    @Query(value = "SELECT * FROM sensors s " +
            "ORDER BY ST_Distance(s.location, ST_GeomFromText(:point, 4326)) " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Sensor> findNearestSensors(@Param("point") String pointWkt,
                                    @Param("limit") int limit);
}