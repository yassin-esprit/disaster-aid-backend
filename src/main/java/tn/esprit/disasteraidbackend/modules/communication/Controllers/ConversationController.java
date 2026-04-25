package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/api/communication/conversations")
@CrossOrigin("*")
@Tag(name = "Conversation", description = "Conversation management")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Operation(summary = "Retrieve conversations")
    @GetMapping("/retrieve-all")
    public ResponseEntity<List<ConversationResponseDTO>> getAll() {
        return ResponseEntity.ok(conversationService.retrieveAllConversations());
    }

    @Operation(summary = "Retrieve my conversations")
    @GetMapping("/me")
    public ResponseEntity<List<ConversationResponseDTO>> getMyConversations() {
        return ResponseEntity.ok(conversationService.getMyConversations());
    }

    @Operation(summary = "Retrieve a conversation by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponseDTO> getById(
            @Parameter(description = "Conversation ID")
            @PathVariable Integer id) {
        return ResponseEntity.ok(conversationService.retrieveConversation(id));
    }

    @Operation(summary = "Create a new conversation")
    @PostMapping("/add")
    public ResponseEntity<ConversationResponseDTO> add(@RequestBody ConversationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationService.addConversation(dto));
    }

    @Operation(summary = "Update a conversation")
    @PutMapping("/update/{id}")
    public ResponseEntity<ConversationResponseDTO> update(
            @Parameter(description = "Conversation ID to update")
            @PathVariable Integer id,
            @RequestBody ConversationRequestDTO dto) {
        return ResponseEntity.ok(conversationService.updateConversation(id, dto));
    }

    @Operation(summary = "Delete a conversation by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Conversation ID to delete")
            @PathVariable Integer id) {
        conversationService.removeConversation(id);
        return ResponseEntity.noContent().build();
    }
}