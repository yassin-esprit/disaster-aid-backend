package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ResourceUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Resource;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.ResourceRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.UserRepository;

@AllArgsConstructor
@Service
public class ResourceServiceImpl implements IResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    @Override
    public Resource addResource(ResourceCreateDto dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getOwnerId()));

        Resource r = new Resource();
        r.setOwner(owner);
        r.setType(dto.getType());
        r.setName(dto.getName());
        r.setQuantity(dto.getQuantity());
        r.setCondition(dto.getCondition());
        r.setLocationUrl(dto.getLocationUrl());
        r.setStatus(dto.getStatus());
        r.setAllocatedToRequestId(dto.getAllocatedToRequestId());

        return resourceRepository.save(r);
    }

    @Override
    public Resource getResource(Integer id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found: " + id));
    }

    @Override
    public Resource updateResource(Integer id, ResourceUpdateDto dto) {
        Resource r = getResource(id);

        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getOwnerId()));

        r.setOwner(owner);
        r.setType(dto.getType());
        r.setName(dto.getName());
        r.setQuantity(dto.getQuantity());
        r.setCondition(dto.getCondition());
        r.setLocationUrl(dto.getLocationUrl());
        r.setStatus(dto.getStatus());
        r.setAllocatedToRequestId(dto.getAllocatedToRequestId());

        return resourceRepository.save(r);
    }

    @Override
    public void deleteResource(Integer id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
}