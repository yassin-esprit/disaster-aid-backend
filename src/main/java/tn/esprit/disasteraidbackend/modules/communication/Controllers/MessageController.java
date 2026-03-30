package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.MessageResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/communication/messages")
@CrossOrigin("*")
@Tag(name = "Message", description = "Gestion des messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Récupérer tous les messages")
    @GetMapping("/retrieve-all")
    public List<MessageResponseDTO> getAll() {
        return messageService.retrieveAllMessages();
    }

    @Operation(summary = "Récupérer un message par ID")
    @GetMapping("/{id}")
    public MessageResponseDTO getById(
            @Parameter(description = "ID du message")
            @PathVariable Integer id) {
        return messageService.retrieveMessage(id);
    }

    @Operation(summary = "Ajouter un message")
    @PostMapping("/add")
    public MessageResponseDTO add(@RequestBody MessageRequestDTO dto) {
        return messageService.addMessage(dto);
    }

    @Operation(summary = "Mettre à jour un message")
    @PutMapping("/update/{id}")
    public MessageResponseDTO update(
            @Parameter(description = "ID du message à modifier")
            @PathVariable Integer id,
            @RequestBody MessageRequestDTO dto) {
        return messageService.updateMessage(id, dto);
    }

    @Operation(summary = "Supprimer un message")
    @DeleteMapping("/delete/{id}")
    public void delete(
            @Parameter(description = "ID du message à supprimer")
            @PathVariable Integer id) {
        messageService.removeMessage(id);
    }
}