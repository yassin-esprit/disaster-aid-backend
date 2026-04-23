package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.DiscussionRequestCreateDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.ConversationRequest;
import tn.esprit.disasteraidbackend.modules.communication.Services.ConversationRequestService;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/conversation-requests")
@CrossOrigin(origins = "*")
public class ConversationRequestController {

    private final ConversationRequestService conversationRequestService;

    public ConversationRequestController(ConversationRequestService conversationRequestService) {
        this.conversationRequestService = conversationRequestService;
    }

    @PostMapping("/citizen/{citizenId}")
    public ResponseEntity<ConversationRequest> createRequest(
            @PathVariable Integer citizenId,
            @RequestBody DiscussionRequestCreateDTO dto
    ) {
        ConversationRequest createdRequest = conversationRequestService.createRequest(citizenId, dto);
        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ConversationRequest>> getPendingRequests() {
        return ResponseEntity.ok(conversationRequestService.getPendingRequests());
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<ConversationRequest>> getRequestsByCitizen(@PathVariable Integer citizenId) {
        return ResponseEntity.ok(conversationRequestService.getRequestsByCitizen(citizenId));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<ConversationRequest>> getRequestsByAdmin(@PathVariable Integer adminId) {
        return ResponseEntity.ok(conversationRequestService.getRequestsByAdmin(adminId));
    }

    @PutMapping("/{requestId}/accept/admin/{adminId}")
    public ResponseEntity<ConversationRequest> acceptRequest(
            @PathVariable Integer requestId,
            @PathVariable Integer adminId
    ) {
        ConversationRequest updatedRequest = conversationRequestService.acceptRequest(requestId, adminId);
        return ResponseEntity.ok(updatedRequest);
    }

    @PutMapping("/{requestId}/reject/admin/{adminId}")
    public ResponseEntity<ConversationRequest> rejectRequest(
            @PathVariable Integer requestId,
            @PathVariable Integer adminId
    ) {
        ConversationRequest updatedRequest = conversationRequestService.rejectRequest(requestId, adminId);
        return ResponseEntity.ok(updatedRequest);
    }
    @PostMapping("/predict")
    public ResponseEntity<AiPredictionResponseDTO> predictRequest(
            @RequestBody DiscussionRequestCreateDTO dto
    ) {
        AiPredictionResponseDTO prediction = conversationRequestService.predictRequest(dto);
        return ResponseEntity.ok(prediction);
    }
}