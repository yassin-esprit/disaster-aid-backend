package tn.esprit.disasteraidbackend.modules.hours.service;

import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetResponse;
import tn.esprit.disasteraidbackend.modules.hours.entity.TimesheetStatus;

import java.util.List;

public interface TimesheetService {

    TimesheetResponse createTimesheet(TimesheetRequest request);

    TimesheetResponse getTimesheetById(Long id);

    List<TimesheetResponse> getAllTimesheets();

    List<TimesheetResponse> getTimesheetsByVolunteer(Long volunteerId);

    List<TimesheetResponse> getTimesheetsByOrg(Long orgId);

    List<TimesheetResponse> getTimesheetsBySupervisor(Long supervisorId);

    TimesheetResponse updateTimesheet(Long id, TimesheetRequest request);

    TimesheetResponse approveTimesheet(Long id, Long supervisorId);

    TimesheetResponse rejectTimesheet(Long id, Long supervisorId, String reason);

    void deleteTimesheet(Long id);
}
