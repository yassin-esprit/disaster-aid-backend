package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.TypingDTO;

@Controller
public class TypingController {

    private final SimpMessagingTemplate messagingTemplate;

    public TypingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/typing")
    public void typing(@Payload TypingDTO typingDTO) {
        messagingTemplate.convertAndSend(
                "/topic/typing/" + typingDTO.getConversationId(),
                typingDTO
        );
    }
}