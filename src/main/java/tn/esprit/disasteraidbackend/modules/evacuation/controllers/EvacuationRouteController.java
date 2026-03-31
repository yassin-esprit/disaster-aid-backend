package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute;
import tn.esprit.disasteraidbackend.modules.evacuation.services.EvacuationRouteService;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evacuation-routes")
@RequiredArgsConstructor
public class EvacuationRouteController {

    private final EvacuationRouteService evacuationRouteService;

    @PostMapping
    public ResponseEntity<EvacuationRoute> createRoute(@RequestBody EvacuationRoute route) {
        EvacuationRoute created = evacuationRouteService.createRoute(route);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvacuationRoute> getRouteById(@PathVariable Integer id) {
        return evacuationRouteService.getRouteById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EvacuationRoute>> getAllRoutes() {
        return new ResponseEntity<>(evacuationRouteService.getAllRoutes(), HttpStatus.OK);
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<EvacuationRoute>> getByAlert(@PathVariable Integer alertId) {
        Alert alert = new Alert();
        alert.setId(alertId);
        return new ResponseEntity<>(evacuationRouteService.getRoutesByAlert(alert), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EvacuationRoute>> getByStatus(@PathVariable EvacuationRoute.RouteStatus status) {
        return new ResponseEntity<>(evacuationRouteService.getRoutesByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<List<EvacuationRoute>> getByShelter(@PathVariable Integer shelterId) {
        return new ResponseEntity<>(evacuationRouteService.getRoutesByShelter(shelterId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvacuationRoute> updateRoute(@PathVariable Integer id, @RequestBody EvacuationRoute route) {
        try {
            EvacuationRoute updated = evacuationRouteService.updateRoute(id, route);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EvacuationRoute> activateRoute(@PathVariable Integer id) {
        try {
            EvacuationRoute activated = evacuationRouteService.activateRoute(id);
            return new ResponseEntity<>(activated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<EvacuationRoute> closeRoute(@PathVariable Integer id) {
        try {
            EvacuationRoute closed = evacuationRouteService.closeRoute(id);
            return new ResponseEntity<>(closed, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Integer id) {
        try {
            evacuationRouteService.deleteRoute(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}