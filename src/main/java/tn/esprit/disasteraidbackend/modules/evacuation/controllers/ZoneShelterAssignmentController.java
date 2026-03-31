package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.ZoneShelterAssignment;
import tn.esprit.disasteraidbackend.modules.evacuation.services.ZoneShelterAssignmentService;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
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
    public ResponseEntity<ZoneShelterAssignment> createAssignment(@RequestBody ZoneShelterAssignment assignment) {
        ZoneShelterAssignment created = assignmentService.createAssignment(assignment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneShelterAssignment> getAssignmentById(@PathVariable Integer id) {
        return assignmentService.getAssignmentById(id)
                .map(assignment -> new ResponseEntity<>(assignment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ZoneShelterAssignment>> getAllAssignments() {
        return new ResponseEntity<>(assignmentService.getAllAssignments(), HttpStatus.OK);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ZoneShelterAssignment>> getByZone(@PathVariable Integer zoneId) {
        GeofenceZone zone = new GeofenceZone();
        zone.setId(zoneId);
        return new ResponseEntity<>(assignmentService.getAssignmentsByZone(zone), HttpStatus.OK);
    }

    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<List<ZoneShelterAssignment>> getByShelter(@PathVariable Integer shelterId) {
        return new ResponseEntity<>(assignmentService.getAssignmentsByShelter(shelterId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneShelterAssignment> updateAssignment(@PathVariable Integer id, @RequestBody ZoneShelterAssignment assignment) {
        try {
            ZoneShelterAssignment updated = assignmentService.updateAssignment(id, assignment);
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