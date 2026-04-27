package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.entity.AlertSnapshot;
import tn.esprit.disasteraidbackend.alert.repository.AlertSnapshotRepository;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/alerts/replay")
public class ReplayController {

    private final AlertSnapshotRepository snapshotRepo;

    public ReplayController(AlertSnapshotRepository snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    @GetMapping("/{alertId}")
    public ApiResponse<List<AlertSnapshot>> getTimeline(@PathVariable Integer alertId) {
        return ApiResponse.ok(snapshotRepo.findByAlertIdOrderBySnapshotAtAsc(alertId));
    }
}
