package tn.esprit.disasteraidbackend.modules.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.hours.entity.Timesheet;
import tn.esprit.disasteraidbackend.modules.hours.entity.TimesheetStatus;

import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    List<Timesheet> findByVolunteerId(Long volunteerId);

    List<Timesheet> findByOrgId(Long orgId);

    List<Timesheet> findBySupervisorId(Long supervisorId);

    List<Timesheet> findByStatus(TimesheetStatus status);

    List<Timesheet> findByVolunteerIdAndStatus(Long volunteerId, TimesheetStatus status);
}
