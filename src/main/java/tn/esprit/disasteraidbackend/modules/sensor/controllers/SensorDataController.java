package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.dto.SensorDataDTO;
import tn.esprit.disasteraidbackend.modules.sensor.services.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @PostMapping
    public ResponseEntity<SensorDataDTO> recordReading(@RequestBody SensorDataDTO readingDTO) {
        SensorDataDTO saved = sensorDataService.recordReading(readingDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<SensorDataDTO>> getBySensor(@PathVariable Integer sensorId) {
        List<SensorDataDTO> readings = sensorDataService.getReadingsBySensorId(sensorId);
        return new ResponseEntity<>(readings, HttpStatus.OK);
    }

    @GetMapping("/sensor/{sensorId}/between")
    public ResponseEntity<List<SensorDataDTO>> getBetweenDates(@PathVariable Integer sensorId,
                                                               @RequestParam LocalDateTime start,
                                                               @RequestParam LocalDateTime end) {
        List<SensorDataDTO> readings = sensorDataService.getReadingsBySensorIdBetweenDates(sensorId, start, end);
        return new ResponseEntity<>(readings, HttpStatus.OK);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<SensorDataDTO>> getAnomalies() {
        return new ResponseEntity<>(sensorDataService.getAnomalies(), HttpStatus.OK);
    }

    @GetMapping("/after")
    public ResponseEntity<List<SensorDataDTO>> getAfter(@RequestParam LocalDateTime date) {
        return new ResponseEntity<>(sensorDataService.getReadingsAfter(date), HttpStatus.OK);
    }
}