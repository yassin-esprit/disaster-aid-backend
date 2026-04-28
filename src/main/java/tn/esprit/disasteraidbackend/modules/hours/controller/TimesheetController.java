package tn.esprit.disasteraidbackend.modules.hours.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.TimesheetResponse;
import tn.esprit.disasteraidbackend.modules.hours.service.TimesheetService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<TimesheetResponse> createTimesheet(@RequestBody TimesheetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timesheetService.createTimesheet(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<TimesheetResponse>> getAllTimesheets() {
        return ResponseEntity.ok(timesheetService.getAllTimesheets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetResponse> getTimesheetById(@PathVariable Long id) {
        return ResponseEntity.ok(timesheetService.getTimesheetById(id));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<TimesheetResponse>> getByVolunteer(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsByVolunteer(volunteerId));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<TimesheetResponse>> getByOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsByOrg(orgId));
    }

    @GetMapping("/supervisor/{supervisorId}")
    public ResponseEntity<List<TimesheetResponse>> getBySupervisor(@PathVariable Long supervisorId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsBySupervisor(supervisorId));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetResponse> updateTimesheet(
            @PathVariable Long id,
            @RequestBody TimesheetRequest request) {
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<TimesheetResponse> approveTimesheet(
            @PathVariable Long id,
            @RequestParam Long supervisorId) {
        return ResponseEntity.ok(timesheetService.approveTimesheet(id, supervisorId));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<TimesheetResponse> rejectTimesheet(
            @PathVariable Long id,
            @RequestParam Long supervisorId,
            @RequestParam String reason) {
        return ResponseEntity.ok(timesheetService.rejectTimesheet(id, supervisorId, reason));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
        return ResponseEntity.noContent().build();
    }
}
