package tn.esprit.disasteraidbackend.modules.opportunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.ApplicationRequestDto;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.ApplicationResponseDto;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.Application;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.Opportunity;
import tn.esprit.disasteraidbackend.modules.opportunity.repository.ApplicationRepository;
import tn.esprit.disasteraidbackend.modules.opportunity.repository.OpportunityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final Set<String> ALLOWED_STATUS = Set.of("PENDING", "ACCEPTED", "REJECTED", "WITHDRAWN");

    private final ApplicationRepository repository;
    private final OpportunityRepository opportunityRepository;

    public List<ApplicationResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ApplicationResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public List<ApplicationResponseDto> getByOpportunity(Integer opportunityId) {
        return repository.findByOpportunityId(opportunityId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ApplicationResponseDto> getByVolunteer(Integer volunteerId) {
        return repository.findByVolunteerId(volunteerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ApplicationResponseDto apply(ApplicationRequestDto dto) {
        Opportunity opportunity = findOpportunityById(dto.getOpportunityId());
        if (!"OPEN".equals(opportunity.getStatus())) {
            throw new RuntimeException("Opportunity is not open: " + dto.getOpportunityId());
        }
        if (opportunity.getFilledSlots() >= opportunity.getSlots()) {
            throw new RuntimeException("Opportunity is full: " + dto.getOpportunityId());
        }
        repository.findByOpportunityIdAndVolunteerId(dto.getOpportunityId(), dto.getVolunteerId())
                .ifPresent(app -> {
                    if (!"WITHDRAWN".equals(app.getStatus())) {
                        throw new RuntimeException("Volunteer already applied for this opportunity");
                    }
                });
        Application entity = toEntity(dto);
        return toResponse(repository.save(entity));
    }

    public ApplicationResponseDto acceptApplication(Integer id) {
        Application application = findById(id);
        Opportunity opportunity = findOpportunityById(application.getOpportunityId());
        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("Only pending applications can be accepted");
        }
        if (opportunity.getFilledSlots() >= opportunity.getSlots()) {
            throw new RuntimeException("Opportunity is full: " + opportunity.getId());
        }
        application.setStatus("ACCEPTED");
        application.setDecidedAt(LocalDateTime.now());
        opportunity.setFilledSlots(opportunity.getFilledSlots() + 1);
        opportunityRepository.save(opportunity);
        return toResponse(repository.save(application));
    }

    public ApplicationResponseDto rejectApplication(Integer id) {
        Application application = findById(id);
        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("Only pending applications can be rejected");
        }
        application.setStatus("REJECTED");
        application.setDecidedAt(LocalDateTime.now());
        return toResponse(repository.save(application));
    }

    public ApplicationResponseDto withdrawApplication(Integer id) {
        Application application = findById(id);
        if (!ALLOWED_STATUS.contains(application.getStatus())) {
            throw new RuntimeException("Invalid application status: " + application.getStatus());
        }
        if ("ACCEPTED".equals(application.getStatus())) {
            Opportunity opportunity = findOpportunityById(application.getOpportunityId());
            if (opportunity.getFilledSlots() > 0) {
                opportunity.setFilledSlots(opportunity.getFilledSlots() - 1);
                opportunityRepository.save(opportunity);
            }
        }
        application.setStatus("WITHDRAWN");
        return toResponse(repository.save(application));
    }

    private Application findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));
    }

    private Opportunity findOpportunityById(Integer id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found: " + id));
    }

    private Application toEntity(ApplicationRequestDto dto) {
        Application application = new Application();
        application.setOpportunityId(dto.getOpportunityId());
        application.setVolunteerId(dto.getVolunteerId());
        application.setMotivationLetter(dto.getMotivationLetter());
        application.setStatus("PENDING");
        return application;
    }

    private ApplicationResponseDto toResponse(Application entity) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(entity.getId());
        dto.setOpportunityId(entity.getOpportunityId());
        dto.setVolunteerId(entity.getVolunteerId());
        dto.setStatus(entity.getStatus());
        dto.setMotivationLetter(entity.getMotivationLetter());
        dto.setMlMatchScore(entity.getMlMatchScore());
        dto.setDecidedAt(entity.getDecidedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
