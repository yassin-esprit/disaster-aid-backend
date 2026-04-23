package tn.esprit.disasteraidbackend.modules.communication.Services;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.DiscussionRequestCreateDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.ConversationRequest;

import java.util.List;

public interface ConversationRequestService {

    ConversationRequest createRequest(Integer citizenId, DiscussionRequestCreateDTO dto);

    List<ConversationRequest> getPendingRequests();

    List<ConversationRequest> getRequestsByCitizen(Integer citizenId);

    List<ConversationRequest> getRequestsByAdmin(Integer adminId);

    ConversationRequest acceptRequest(Integer requestId, Integer adminId);

    ConversationRequest rejectRequest(Integer requestId, Integer adminId);

    AiPredictionResponseDTO predictRequest(DiscussionRequestCreateDTO dto);
}