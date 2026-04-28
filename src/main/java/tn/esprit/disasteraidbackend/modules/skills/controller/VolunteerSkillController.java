package tn.esprit.disasteraidbackend.modules.skills.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.skills.dto.VolunteerSkillRequestDto;
import tn.esprit.disasteraidbackend.modules.skills.dto.VolunteerSkillResponseDto;
import tn.esprit.disasteraidbackend.modules.skills.service.VolunteerSkillService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/volunteer-skills")
@RequiredArgsConstructor
public class VolunteerSkillController {

    private final VolunteerSkillService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VolunteerSkillResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerSkillResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/volunteer/{vid}")
    public ResponseEntity<ApiResponse<List<VolunteerSkillResponseDto>>> getByVolunteerId(@PathVariable Integer vid) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByVolunteerId(vid)));
    }

    @GetMapping("/skill/{sid}")
    public ResponseEntity<ApiResponse<List<VolunteerSkillResponseDto>>> getBySkillId(@PathVariable Integer sid) {
        return ResponseEntity.ok(ApiResponse.ok(service.getBySkillId(sid)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VolunteerSkillResponseDto>> create(
            @Valid @RequestBody VolunteerSkillRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Volunteer skill created", service.addSkillToVolunteer(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerSkillResponseDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody VolunteerSkillRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Updated", service.updateProficiency(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{id}/endorse")
    public ResponseEntity<ApiResponse<VolunteerSkillResponseDto>> endorse(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Endorsed", service.endorse(id)));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<VolunteerSkillResponseDto>> verify(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Verified", service.verify(id)));
    }
}
