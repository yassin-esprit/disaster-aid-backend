package tn.esprit.disasteraidbackend.spatial;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.mapper.DTOMapper;
import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDTO;
import tn.esprit.disasteraidbackend.modules.sensor.repositories.SensorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spatial")
@RequiredArgsConstructor
public class SpatialQueryController {

    private final SensorRepository sensorRepository;

    @GetMapping("/sensors/inside-zone")
    public ResponseEntity<List<SensorDTO>> sensorsInsideZone(@RequestParam String polygon) {
        return ResponseEntity.ok(
                sensorRepository.findSensorsInsidePolygon(polygon)
                        .stream().map(DTOMapper::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/sensors/nearest")
    public ResponseEntity<List<SensorDTO>> nearestSensors(
            @RequestParam String point,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(
                sensorRepository.findNearestSensors(point, limit)
                        .stream().map(DTOMapper::toDTO).collect(Collectors.toList()));
    }
}
