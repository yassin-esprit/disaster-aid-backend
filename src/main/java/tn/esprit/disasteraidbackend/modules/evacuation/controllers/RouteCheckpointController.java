package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.RouteCheckpoint;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.services.RouteCheckpointService;
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
    public ResponseEntity<RouteCheckpoint> createCheckpoint(@RequestBody RouteCheckpoint checkpoint) {
        RouteCheckpoint created = checkpointService.createCheckpoint(checkpoint);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteCheckpoint> getCheckpointById(@PathVariable Integer id) {
        return checkpointService.getCheckpointById(id)
                .map(checkpoint -> new ResponseEntity<>(checkpoint, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<RouteCheckpoint>> getAllCheckpoints() {
        return new ResponseEntity<>(checkpointService.getAllCheckpoints(), HttpStatus.OK);
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<RouteCheckpoint>> getByRoute(@PathVariable Integer routeId) {
        EvacuationRoute route = new EvacuationRoute();
        route.setId(routeId);
        return new ResponseEntity<>(checkpointService.getCheckpointsByRoute(route), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RouteCheckpoint>> getByType(@PathVariable RouteCheckpoint.CheckpointType type) {
        return new ResponseEntity<>(checkpointService.getCheckpointsByType(type), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteCheckpoint> updateCheckpoint(@PathVariable Integer id, @RequestBody RouteCheckpoint checkpoint) {
        try {
            RouteCheckpoint updated = checkpointService.updateCheckpoint(id, checkpoint);
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