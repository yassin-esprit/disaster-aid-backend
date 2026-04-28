package tn.esprit.disasteraidbackend.modules.skills.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.skills.dto.SkillRequestDto;
import tn.esprit.disasteraidbackend.modules.skills.dto.SkillResponseDto;
import tn.esprit.disasteraidbackend.modules.skills.service.SkillService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponseDto>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("/category/{cat}")
    public ResponseEntity<ApiResponse<List<SkillResponseDto>>> getByCategory(@PathVariable String cat) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByCategory(cat)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponseDto>> create(@Valid @RequestBody SkillRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Skill created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponseDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody SkillRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<SkillResponseDto>> toggle(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok("Toggled", service.toggleActive(id)));
    }
}
