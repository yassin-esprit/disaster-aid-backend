package tn.esprit.disasteraidbackend.modules.resourceShelter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.URL;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Condition;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ResourceStatus;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ResourceType;

public class ResourceCreateDto {

    @NotNull
    private Integer ownerId;

    @NotNull
    private ResourceType type;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    private Condition condition;

    @NotBlank
    @URL
    private String locationUrl;

    @NotNull
    private ResourceStatus status;

    private Integer allocatedToRequestId;

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
    public ResourceType getType() { return type; }
    public void setType(ResourceType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
    public String getLocationUrl() { return locationUrl; }
    public void setLocationUrl(String locationUrl) { this.locationUrl = locationUrl; }
    public ResourceStatus getStatus() { return status; }
    public void setStatus(ResourceStatus status) { this.status = status; }
    public Integer getAllocatedToRequestId() { return allocatedToRequestId; }
    public void setAllocatedToRequestId(Integer allocatedToRequestId) { this.allocatedToRequestId = allocatedToRequestId; }
}