package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDataDTO;
import tn.esprit.disasteraidbackend.modules.sensor.services.SensorDataService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;


    @PostMapping
    public ResponseEntity<SensorDataDTO> record(@RequestBody SensorDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sensorDataService.recordReading(dto));
    }


    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<SensorDataDTO>> getBySensor(
            @PathVariable Integer sensorId,
            @PageableDefault(size = 20, sort = "recordedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(sensorDataService.getReadingsBySensorId(sensorId, pageable));
    }


    @GetMapping("/sensor/{sensorId}/between")
    public ResponseEntity<List<SensorDataDTO>> getBetweenDates(
            @PathVariable Integer sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(
                sensorDataService.getReadingsBySensorIdBetweenDates(sensorId, start, end));
    }


    @GetMapping("/anomalies")
    public ResponseEntity<Page<SensorDataDTO>> getAnomalies(
            @PageableDefault(size = 20, sort = "recordedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok((Page<SensorDataDTO>) sensorDataService.getAnomalies());
    }
}
