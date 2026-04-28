package tn.esprit.disasteraidbackend.modules.opportunity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.ApplicationRequestDto;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.ApplicationResponseDto;
import tn.esprit.disasteraidbackend.modules.opportunity.service.ApplicationService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/opportunity/{oid}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getByOpportunity(@PathVariable Integer oid) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByOpportunity(oid)));
    }

    @GetMapping("/volunteer/{vid}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getByVolunteer(@PathVariable Integer vid) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByVolunteer(vid)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> apply(@Valid @RequestBody ApplicationRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Application submitted", service.apply(dto)));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> accept(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Accepted", service.acceptApplication(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> reject(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Rejected", service.rejectApplication(id)));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> withdraw(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Withdrawn", service.withdrawApplication(id)));
    }
}
