package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Services.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/communication/conversations")
@CrossOrigin("*")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/retrieve-all")
    public List<Conversation> getAll() {
        return conversationService.retrieveAllConversations();
    }

    @GetMapping("/{id}")
    public Conversation getById(@PathVariable Integer id) {
        return conversationService.retrieveConversation(id);
    }

    @PostMapping("/add")
    public Conversation add(@RequestBody Conversation conversation) {
        return conversationService.addConversation(conversation);
    }

    @PutMapping("/update")
    public Conversation update(@RequestBody Conversation conversation) {
        return conversationService.updateConversation(conversation);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        conversationService.removeConversation(id);
    }
}