package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.dto.ZoneShelterAssignmentDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.services.ZoneShelterAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zone-shelter-assignments")
@RequiredArgsConstructor
public class ZoneShelterAssignmentController {

    private final ZoneShelterAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<ZoneShelterAssignmentDTO> createAssignment(@RequestBody ZoneShelterAssignmentDTO assignmentDTO) {
        ZoneShelterAssignmentDTO created = assignmentService.createAssignment(assignmentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneShelterAssignmentDTO> getAssignmentById(@PathVariable Integer id) {
        return assignmentService.getAssignmentById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ZoneShelterAssignmentDTO>> getAllAssignments() {
        return new ResponseEntity<>(assignmentService.getAllAssignments(), HttpStatus.OK);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ZoneShelterAssignmentDTO>> getByZone(@PathVariable Integer zoneId) {
        return new ResponseEntity<>(assignmentService.getAssignmentsByZoneId(zoneId), HttpStatus.OK);
    }

    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<List<ZoneShelterAssignmentDTO>> getByShelter(@PathVariable Integer shelterId) {
        return new ResponseEntity<>(assignmentService.getAssignmentsByShelter(shelterId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneShelterAssignmentDTO> updateAssignment(@PathVariable Integer id, @RequestBody ZoneShelterAssignmentDTO assignmentDTO) {
        try {
            ZoneShelterAssignmentDTO updated = assignmentService.updateAssignment(id, assignmentDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        try {
            assignmentService.deleteAssignment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}