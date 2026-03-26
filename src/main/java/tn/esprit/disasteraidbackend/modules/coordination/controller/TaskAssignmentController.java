package tn.esprit.disasteraidbackend.modules.coordination.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;
import tn.esprit.disasteraidbackend.modules.coordination.service.TaskAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskAssignment>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskAssignment>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/help-request/{helpRequestId}")
    public ResponseEntity<ApiResponse<List<TaskAssignment>>> getByHelpRequestId(
            @PathVariable Integer helpRequestId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByHelpRequestId(helpRequestId)));
    }

    @PostMapping("/help-request/{helpRequestId}")
    public ResponseEntity<ApiResponse<TaskAssignment>> create(
            @PathVariable Integer helpRequestId,
            @RequestBody TaskAssignment assignment) {
        return ResponseEntity.ok(ApiResponse.ok("Assignment created",
                service.create(helpRequestId, assignment)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskAssignment>> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated",
                service.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
