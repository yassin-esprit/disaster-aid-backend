package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Resource;

import java.util.List;

public interface IResourceService {
    Resource addResource(ResourceCreateDto dto);
    Resource getResource(Integer id);
    Resource updateResource(Integer id, ResourceUpdateDto dto);
    void deleteResource(Integer id);
    List<Resource> getAllResources();
}