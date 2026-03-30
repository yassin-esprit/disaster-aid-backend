package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.ConversationResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/communication/conversations")
@CrossOrigin("*")
@Tag(name = "Conversation", description = "Gestion des conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Operation(summary = "Récupérer toutes les conversations")
    @GetMapping("/retrieve-all")
    public List<ConversationResponseDTO> getAll() {
        return conversationService.retrieveAllConversations();
    }

    @Operation(summary = "Récupérer une conversation par ID")
    @GetMapping("/{id}")
    public ConversationResponseDTO getById(
            @Parameter(description = "ID de la conversation")
            @PathVariable Integer id) {
        return conversationService.retrieveConversation(id);
    }

    @Operation(summary = "Ajouter une nouvelle conversation")
    @PostMapping("/add")
    public ConversationResponseDTO add(@RequestBody ConversationRequestDTO dto) {
        return conversationService.addConversation(dto);
    }

    @Operation(summary = "Mettre à jour une conversation")
    @PutMapping("/update/{id}")
    public ConversationResponseDTO update(
            @Parameter(description = "ID de la conversation à modifier")
            @PathVariable Integer id,
            @RequestBody ConversationRequestDTO dto) {
        return conversationService.updateConversation(id, dto);
    }

    @Operation(summary = "Supprimer une conversation par ID")
    @DeleteMapping("/delete/{id}")
    public void delete(
            @Parameter(description = "ID de la conversation à supprimer")
            @PathVariable Integer id) {
        conversationService.removeConversation(id);
    }
}