package tn.esprit.disasteraidbackend.modules.opportunity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityRequestDto;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityResponseDto;
import tn.esprit.disasteraidbackend.modules.opportunity.service.OpportunityService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpportunityResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OpportunityResponseDto>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<ApiResponse<List<OpportunityResponseDto>>> getByOrgId(@PathVariable Integer orgId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByOrgId(orgId)));
    }

    @GetMapping("/category/{cat}")
    public ResponseEntity<ApiResponse<List<OpportunityResponseDto>>> getByCategory(@PathVariable String cat) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByCategory(cat)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OpportunityResponseDto>> create(@Valid @RequestBody OpportunityRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Opportunity created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityResponseDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody OpportunityRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<OpportunityResponseDto>> close(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Closed", service.closeOpportunity(id)));
    }
}
