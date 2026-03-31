package tn.esprit.disasteraidbackend.analytics.m13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.AuditLog;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findTop100ByOrderByTimestampDesc();

    long countByActorId(Integer actorId);
}

