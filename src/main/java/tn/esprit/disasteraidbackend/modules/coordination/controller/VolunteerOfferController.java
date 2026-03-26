package tn.esprit.disasteraidbackend.modules.coordination.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.entity.VolunteerOffer;
import tn.esprit.disasteraidbackend.modules.coordination.service.VolunteerOfferService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer-offers")
@RequiredArgsConstructor
public class VolunteerOfferController {

    private final VolunteerOfferService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VolunteerOffer>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerOffer>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VolunteerOffer>> create(@RequestBody VolunteerOffer offer) {
        return ResponseEntity.ok(ApiResponse.ok("Offer created", service.create(offer)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerOffer>> update(
            @PathVariable Integer id,
            @RequestBody VolunteerOffer offer) {
        return ResponseEntity.ok(ApiResponse.ok("Offer updated", service.update(id, offer)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<VolunteerOffer>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByStatus(status)));
    }
}