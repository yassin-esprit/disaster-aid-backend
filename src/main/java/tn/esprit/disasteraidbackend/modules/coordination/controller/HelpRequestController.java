package tn.esprit.disasteraidbackend.modules.coordination.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.dto.HelpRequestRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.HelpRequestResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.service.HelpRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {

    private final HelpRequestService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HelpRequestResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HelpRequestResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HelpRequestResponseDto>> create(
            @RequestBody HelpRequestRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Help request created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HelpRequestResponseDto>> update(
            @PathVariable Integer id,
            @RequestBody HelpRequestRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Help request updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<HelpRequestResponseDto>>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }
}