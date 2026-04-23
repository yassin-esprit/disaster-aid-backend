package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.VoiceCallSignalDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Conversation;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoiceCallService {

    private final ConversationRepository conversationRepository;

    public VoiceCallService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Transactional(readOnly = true)
    public VoiceCallSignalDTO validateAndPrepareSignal(VoiceCallSignalDTO signalDTO) {
        if (signalDTO == null) {
            throw new RuntimeException("Voice call signal is required");
        }
        if (signalDTO.getConversationId() == null) {
            throw new RuntimeException("conversationId is required");
        }
        if (signalDTO.getFromUserId() == null) {
            throw new RuntimeException("fromUserId is required");
        }
        if (signalDTO.getType() == null) {
            throw new RuntimeException("signal type is required");
        }

        Conversation conversation = conversationRepository.findByIdWithParticipants(signalDTO.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        List<Integer> participantIds = conversation.getParticipantIds();
        if (participantIds == null || !participantIds.contains(signalDTO.getFromUserId())) {
            throw new RuntimeException("Sender is not a participant of this conversation");
        }

        if (signalDTO.getToUserId() != null && !participantIds.contains(signalDTO.getToUserId())) {
            throw new RuntimeException("Receiver is not a participant of this conversation");
        }

        if (signalDTO.getTimestamp() == null) {
            signalDTO.setTimestamp(LocalDateTime.now());
        }

        return signalDTO;
    }

    public String getTopic(Integer conversationId) {
        return "/topic/voicecall/" + conversationId;
    }
}
