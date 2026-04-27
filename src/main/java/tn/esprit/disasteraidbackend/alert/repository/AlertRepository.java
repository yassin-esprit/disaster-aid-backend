package tn.esprit.disasteraidbackend.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.entity.AlertSeverity;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

    List<Alert> findByStatusOrderBySeverityAscReportedAtDesc(AlertStatus status);

    List<Alert> findByStatusInOrderBySeverityAscReportedAtDesc(List<AlertStatus> statuses);

    List<Alert> findBySeverityOrderByReportedAtDesc(AlertSeverity severity);

    long countByStatus(AlertStatus status);
}
