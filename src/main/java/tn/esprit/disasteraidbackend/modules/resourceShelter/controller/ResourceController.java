package tn.esprit.disasteraidbackend.modules.resourceShelter.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Resource;
import tn.esprit.disasteraidbackend.modules.resourceShelter.service.IResourceService;

@RestController
@RequestMapping("/resource-shelter/resources")
public class ResourceController {

    private final IResourceService resourceService;

    public ResourceController(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public Resource add(@Valid @RequestBody ResourceCreateDto dto) {
        return resourceService.addResource(dto);
    }

    @GetMapping("/{id}")
    public Resource get(@PathVariable Integer id) {
        return resourceService.getResource(id);
    }

    @GetMapping
    public List<Resource> getAll() {
        return resourceService.getAllResources();
    }

    @PutMapping("/{id}")
    public Resource update(@PathVariable Integer id, @Valid @RequestBody ResourceUpdateDto dto) {
        return resourceService.updateResource(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        resourceService.deleteResource(id);
    }
}