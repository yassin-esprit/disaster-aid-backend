package tn.esprit.disasteraidbackend.modules.communication.Controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.VoiceCallSignalDTO;
import tn.esprit.disasteraidbackend.modules.communication.Services.VoiceCallService;

@Controller
public class VoiceCallController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VoiceCallService voiceCallService;

    public VoiceCallController(SimpMessagingTemplate messagingTemplate, VoiceCallService voiceCallService) {
        this.messagingTemplate = messagingTemplate;
        this.voiceCallService = voiceCallService;
    }

    @MessageMapping("/voicecall.signal")
    public void relaySignal(@Payload VoiceCallSignalDTO signalDTO) {
        VoiceCallSignalDTO validatedSignal = voiceCallService.validateAndPrepareSignal(signalDTO);

        messagingTemplate.convertAndSend(
                voiceCallService.getTopic(validatedSignal.getConversationId()),
                validatedSignal
        );
    }
}
