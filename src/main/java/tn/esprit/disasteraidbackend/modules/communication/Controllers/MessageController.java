package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;
import tn.esprit.disasteraidbackend.modules.communication.Services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/communication/messages")
@CrossOrigin("*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/retrieve-all")
    public List<Message> getAll() {
        return messageService.retrieveAllMessages();
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable Integer id) {
        return messageService.retrieveMessage(id);
    }

    @PostMapping("/add")
    public Message add(@RequestBody Message message) {
        return messageService.addMessage(message);
    }

    @PutMapping("/update")
    public Message update(@RequestBody Message message) {
        return messageService.updateMessage(message);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        messageService.removeMessage(id);
    }
}