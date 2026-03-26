package tn.esprit.disasteraidbackend.modules.coordination.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.service.HelpRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {

    private final HelpRequestService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HelpRequest>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HelpRequest>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HelpRequest>> create(@RequestBody HelpRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Help request created", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HelpRequest>> update(
            @PathVariable Integer id,
            @RequestBody HelpRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Help request updated", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<HelpRequest>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }
}
