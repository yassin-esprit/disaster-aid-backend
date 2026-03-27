package tn.esprit.disasteraidbackend.modules.coordination.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.service.VolunteerOfferService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer-offers")
@RequiredArgsConstructor
public class VolunteerOfferController {

    private final VolunteerOfferService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VolunteerOfferResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerOfferResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VolunteerOfferResponseDto>> create(
            @RequestBody VolunteerOfferRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Offer created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerOfferResponseDto>> update(
            @PathVariable Integer id,
            @RequestBody VolunteerOfferRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Offer updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<VolunteerOfferResponseDto>>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }
}