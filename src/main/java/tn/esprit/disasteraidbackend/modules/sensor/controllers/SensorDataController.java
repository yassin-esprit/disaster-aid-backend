package tn.esprit.disasteraidbackend.modules.sensor.controllers;

import tn.esprit.disasteraidbackend.modules.sensor.entities.Sensor;
import tn.esprit.disasteraidbackend.modules.sensor.entities.SensorData;
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
    public ResponseEntity<SensorData> recordReading(@RequestBody SensorData reading) {
        SensorData saved = sensorDataService.recordReading(reading);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<SensorData>> getBySensor(@PathVariable Integer sensorId) {
        // We need to get Sensor entity by id; we can inject SensorRepository or let service handle it
        // Better to create a service method that takes sensorId. We'll adjust SensorDataService accordingly.
        // For now, assume we have a method in service that accepts sensorId.
        List<SensorData> readings = sensorDataService.getReadingsBySensorId(sensorId);
        return new ResponseEntity<>(readings, HttpStatus.OK);
    }

    @GetMapping("/sensor/{sensorId}/between")
    public ResponseEntity<List<SensorData>> getBetweenDates(@PathVariable Integer sensorId,
                                                            @RequestParam LocalDateTime start,
                                                            @RequestParam LocalDateTime end) {
        List<SensorData> readings = sensorDataService.getReadingsBySensorIdBetweenDates(sensorId, start, end);
        return new ResponseEntity<>(readings, HttpStatus.OK);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<SensorData>> getAnomalies() {
        return new ResponseEntity<>(sensorDataService.getAnomalies(), HttpStatus.OK);
    }

    @GetMapping("/after")
    public ResponseEntity<List<SensorData>> getAfter(@RequestParam LocalDateTime date) {
        return new ResponseEntity<>(sensorDataService.getReadingsAfter(date), HttpStatus.OK);
    }
}