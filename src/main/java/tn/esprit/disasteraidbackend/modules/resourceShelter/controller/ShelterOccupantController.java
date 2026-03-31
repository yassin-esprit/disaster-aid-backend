package tn.esprit.disasteraidbackend.modules.resourceShelter.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterOccupant;
import tn.esprit.disasteraidbackend.modules.resourceShelter.service.IShelterOccupantService;

@RestController
@RequestMapping("/resource-shelter/shelter-occupants")
public class ShelterOccupantController {

    private final IShelterOccupantService occupantService;

    public ShelterOccupantController(IShelterOccupantService occupantService) {
        this.occupantService = occupantService;
    }

    // check-in
    @PostMapping("/check-in")
    public ShelterOccupant checkIn(@Valid @RequestBody ShelterOccupantCreateDto dto) {
        return occupantService.checkIn(dto);
    }

    // check-out
    @PostMapping("/{recordId}/check-out")
    public ShelterOccupant checkOut(@PathVariable Integer recordId) {
        return occupantService.checkOut(recordId);
    }

    // CRUD
    @GetMapping("/{id}")
    public ShelterOccupant get(@PathVariable Integer id) {
        return occupantService.getShelterOccupant(id);
    }

    @GetMapping
    public List<ShelterOccupant> getAll() {
        return occupantService.getAllShelterOccupants();
    }

    @GetMapping("/by-shelter/{shelterId}")
    public List<ShelterOccupant> getByShelter(@PathVariable Integer shelterId) {
        return occupantService.getOccupantsByShelterId(shelterId);
    }

    @PutMapping("/{id}")
    public ShelterOccupant update(@PathVariable Integer id, @Valid @RequestBody ShelterOccupantUpdateDto dto) {
        return occupantService.updateShelterOccupant(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        occupantService.deleteShelterOccupant(id);
    }
}