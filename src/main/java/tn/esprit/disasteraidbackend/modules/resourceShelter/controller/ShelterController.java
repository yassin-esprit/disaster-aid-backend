package tn.esprit.disasteraidbackend.modules.resourceShelter.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Shelter;
import tn.esprit.disasteraidbackend.modules.resourceShelter.service.IShelterService;

import java.util.List;

@RestController
@RequestMapping("/resource-shelter/shelters")
public class ShelterController {

    private final IShelterService shelterService;

    public ShelterController(IShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping
    public Shelter add(@Valid @RequestBody ShelterCreateDto dto) {
        return shelterService.addShelter(dto);
    }

    @GetMapping("/{id}")
    public Shelter get(@PathVariable Integer id) {
        return shelterService.getShelter(id);
    }

    @GetMapping
    public List<Shelter> getAll() {
        return shelterService.getAllShelters();
    }

    @PutMapping("/{id}")
    public Shelter update(@PathVariable Integer id, @Valid @RequestBody ShelterUpdateDto dto) {
        return shelterService.updateShelter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        shelterService.deleteShelter(id);
    }
}