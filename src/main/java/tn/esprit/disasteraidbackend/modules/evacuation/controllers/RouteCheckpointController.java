package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.dto.RouteCheckpointDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.services.RouteCheckpointService;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint.CheckpointType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route-checkpoints")
@RequiredArgsConstructor
public class RouteCheckpointController {

    private final RouteCheckpointService checkpointService;

    @PostMapping
    public ResponseEntity<RouteCheckpointDTO> createCheckpoint(@RequestBody RouteCheckpointDTO checkpointDTO) {
        RouteCheckpointDTO created = checkpointService.createCheckpoint(checkpointDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteCheckpointDTO> getCheckpointById(@PathVariable Integer id) {
        return checkpointService.getCheckpointById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<RouteCheckpointDTO>> getAllCheckpoints() {
        return new ResponseEntity<>(checkpointService.getAllCheckpoints(), HttpStatus.OK);
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<RouteCheckpointDTO>> getByRoute(@PathVariable Integer routeId) {
        return new ResponseEntity<>(checkpointService.getCheckpointsByRouteId(routeId), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RouteCheckpointDTO>> getByType(@PathVariable CheckpointType type) {
        return new ResponseEntity<>(checkpointService.getCheckpointsByType(type), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteCheckpointDTO> updateCheckpoint(@PathVariable Integer id, @RequestBody RouteCheckpointDTO checkpointDTO) {
        try {
            RouteCheckpointDTO updated = checkpointService.updateCheckpoint(id, checkpointDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckpoint(@PathVariable Integer id) {
        try {
            checkpointService.deleteCheckpoint(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}