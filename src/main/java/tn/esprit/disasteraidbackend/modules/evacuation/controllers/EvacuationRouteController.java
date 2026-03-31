package tn.esprit.disasteraidbackend.modules.evacuation.controllers;

import tn.esprit.disasteraidbackend.modules.evacuation.dto.EvacuationRouteDTO;
import tn.esprit.disasteraidbackend.modules.evacuation.services.EvacuationRouteService;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.EvacuationRoute.RouteStatus;
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
    public ResponseEntity<EvacuationRouteDTO> createRoute(@RequestBody EvacuationRouteDTO routeDTO) {
        EvacuationRouteDTO created = evacuationRouteService.createRoute(routeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvacuationRouteDTO> getRouteById(@PathVariable Integer id) {
        return evacuationRouteService.getRouteById(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EvacuationRouteDTO>> getAllRoutes() {
        return new ResponseEntity<>(evacuationRouteService.getAllRoutes(), HttpStatus.OK);
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<EvacuationRouteDTO>> getByAlert(@PathVariable Integer alertId) {
        return new ResponseEntity<>(evacuationRouteService.getRoutesByAlertId(alertId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EvacuationRouteDTO>> getByStatus(@PathVariable RouteStatus status) {
        return new ResponseEntity<>(evacuationRouteService.getRoutesByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<List<EvacuationRouteDTO>> getByShelter(@PathVariable Integer shelterId) {
        return new ResponseEntity<>(evacuationRouteService.getRoutesByShelter(shelterId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvacuationRouteDTO> updateRoute(@PathVariable Integer id, @RequestBody EvacuationRouteDTO routeDTO) {
        try {
            EvacuationRouteDTO updated = evacuationRouteService.updateRoute(id, routeDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EvacuationRouteDTO> activateRoute(@PathVariable Integer id) {
        try {
            EvacuationRouteDTO activated = evacuationRouteService.activateRoute(id);
            return new ResponseEntity<>(activated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<EvacuationRouteDTO> closeRoute(@PathVariable Integer id) {
        try {
            EvacuationRouteDTO closed = evacuationRouteService.closeRoute(id);
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