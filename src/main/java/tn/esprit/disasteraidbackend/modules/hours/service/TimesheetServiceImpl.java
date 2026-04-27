package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetResponse;
import tn.esprit.disasteraidbackend.modules.hours.entity.Timesheet;
import tn.esprit.disasteraidbackend.modules.hours.entity.TimesheetStatus;
import tn.esprit.disasteraidbackend.modules.hours.repository.TimesheetRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository timesheetRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private Timesheet toEntity(TimesheetRequest request) {
        return Timesheet.builder()
                .applicationId(request.getApplicationId())
                .volunteerId(request.getVolunteerId())
                .orgId(request.getOrgId())
                .loggedHours(request.getLoggedHours())
                .taskDescription(request.getTaskDescription())
                .workDate(request.getWorkDate())
                .supervisorId(request.getSupervisorId())
                .status(TimesheetStatus.PENDING)
                .build();
    }

    private TimesheetResponse toResponse(Timesheet t) {
        TimesheetResponse response = new TimesheetResponse();
        response.setTimesheetId(t.getTimesheetId());
        response.setApplicationId(t.getApplicationId());
        response.setVolunteerId(t.getVolunteerId());
        response.setOrgId(t.getOrgId());
        response.setLoggedHours(t.getLoggedHours());
        response.setTaskDescription(t.getTaskDescription());
        response.setWorkDate(t.getWorkDate());
        response.setStatus(t.getStatus());
        response.setSupervisorId(t.getSupervisorId());
        response.setApprovedAt(t.getApprovedAt());
        response.setRejectionReason(t.getRejectionReason());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public TimesheetResponse createTimesheet(TimesheetRequest request) {
        Timesheet saved = timesheetRepository.save(toEntity(request));
        return toResponse(saved);
    }

    @Override
    public TimesheetResponse getTimesheetById(Long id) {
        Timesheet t = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));
        return toResponse(t);
    }

    @Override
    public List<TimesheetResponse> getAllTimesheets() {
        return timesheetRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TimesheetResponse> getTimesheetsByVolunteer(Long volunteerId) {
        return timesheetRepository.findByVolunteerId(volunteerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TimesheetResponse> getTimesheetsByOrg(Long orgId) {
        return timesheetRepository.findByOrgId(orgId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TimesheetResponse> getTimesheetsBySupervisor(Long supervisorId) {
        return timesheetRepository.findBySupervisorId(supervisorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TimesheetResponse updateTimesheet(Long id, TimesheetRequest request) {
        Timesheet t = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));
        t.setApplicationId(request.getApplicationId());
        t.setVolunteerId(request.getVolunteerId());
        t.setOrgId(request.getOrgId());
        t.setLoggedHours(request.getLoggedHours());
        t.setTaskDescription(request.getTaskDescription());
        t.setWorkDate(request.getWorkDate());
        t.setSupervisorId(request.getSupervisorId());
        return toResponse(timesheetRepository.save(t));
    }

    @Override
    public TimesheetResponse approveTimesheet(Long id, Long supervisorId) {
        Timesheet t = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));
        t.setStatus(TimesheetStatus.APPROVED);
        t.setSupervisorId(supervisorId);
        t.setApprovedAt(LocalDateTime.now());
        t.setRejectionReason(null);
        return toResponse(timesheetRepository.save(t));
    }

    @Override
    public TimesheetResponse rejectTimesheet(Long id, Long supervisorId, String reason) {
        Timesheet t = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));
        t.setStatus(TimesheetStatus.REJECTED);
        t.setSupervisorId(supervisorId);
        t.setRejectionReason(reason);
        t.setApprovedAt(null);
        return toResponse(timesheetRepository.save(t));
    }

    @Override
    public void deleteTimesheet(Long id) {
        if (!timesheetRepository.existsById(id)) {
            throw new RuntimeException("Timesheet not found with id: " + id);
        }
        timesheetRepository.deleteById(id);
    }
}
