package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.MessageService;

import java.util.List;

@RestController
@RequestMapping({"/communication/messages", "/api/messages"})
@CrossOrigin("*")
@Tag(name = "Message", description = "Message management")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Retrieve messages")
    @GetMapping("/retrieve-all")
    public ResponseEntity<List<MessageResponseDTO>> getAll() {
        return ResponseEntity.ok(messageService.retrieveAllMessages());
    }

    @Operation(summary = "Retrieve my messages")
    @GetMapping("/me")
    public ResponseEntity<List<MessageResponseDTO>> getMyMessages() {
        return ResponseEntity.ok(messageService.getMyMessages());
    }

    @Operation(summary = "Retrieve messages by conversation ID")
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponseDTO>> getByConversation(
            @Parameter(description = "Conversation ID")
            @PathVariable Integer conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @Operation(summary = "Retrieve a message by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> getById(
            @Parameter(description = "Message ID")
            @PathVariable Integer id) {
        return ResponseEntity.ok(messageService.retrieveMessage(id));
    }

    @Operation(summary = "Create a message")
    @PostMapping("/add")
    public ResponseEntity<MessageResponseDTO> add(@RequestBody MessageRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.addMessage(dto));
    }

    @Operation(summary = "Update a message")
    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponseDTO> update(
            @Parameter(description = "Message ID to update")
            @PathVariable Integer id,
            @RequestBody MessageRequestDTO dto) {
        return ResponseEntity.ok(messageService.updateMessage(id, dto));
    }

    @Operation(summary = "Delete a message")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Message ID to delete")
            @PathVariable Integer id) {
        messageService.removeMessage(id);
        return ResponseEntity.noContent().build();
    }
}