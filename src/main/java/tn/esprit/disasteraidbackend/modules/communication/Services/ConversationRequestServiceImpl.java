package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionResponseDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.DiscussionRequestCreateDTO;
import tn.esprit.disasteraidbackend.modules.communication.Entities.*;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.ConversationRequestRepository;
import tn.esprit.disasteraidbackend.modules.communication.Repositories.NotificationRepository;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;
import tn.esprit.disasteraidbackend.modules.identity.Entities.UserRole;
import tn.esprit.disasteraidbackend.modules.identity.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ConversationRequestServiceImpl implements ConversationRequestService {

    private final ConversationRequestRepository conversationRequestRepository;
    private final ConversationRepository conversationRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AiPredictionService aiPredictionService;

    public ConversationRequestServiceImpl(
            ConversationRequestRepository conversationRequestRepository,
            ConversationRepository conversationRepository,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            AiPredictionService aiPredictionService
    ) {
        this.conversationRequestRepository = conversationRequestRepository;
        this.conversationRepository = conversationRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.aiPredictionService = aiPredictionService;
    }

    @Override
    public ConversationRequest createRequest(Integer citizenId, DiscussionRequestCreateDTO dto) {
        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new RuntimeException("Citizen not found with id: " + citizenId));

        AiPredictionResponseDTO aiPrediction = aiPredictionService.predict(
                dto.getSubject(),
                dto.getDescription()
        );

        if (aiPrediction != null) {
            System.out.println("=== AI PREDICTION ===");
            System.out.println("Category : " + aiPrediction.getCategory());
            System.out.println("Priority : " + aiPrediction.getPriority());
            System.out.println("=====================");
        }

        ConversationRequest request = new ConversationRequest();
        request.setCitizenId(citizenId);
        request.setSubject(dto.getSubject());
        request.setDescription(dto.getDescription());
        request.setStatus(ConversationRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        ConversationRequest savedRequest = conversationRequestRepository.save(request);

        List<User> admins = findAdmins();

        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setRecipient(admin);
            notification.setType(NotifType.SYSTEM);

            String title = "Nouvelle demande de discussion";
            String body = "Le citoyen " + citizen.getEmail() + " a envoyé une demande de discussion.";

            if (aiPrediction != null) {
                body += " [IA: " + aiPrediction.getCategory() + " | " + aiPrediction.getPriority() + "]";
            }

            notification.setTitle(title);
            notification.setBody(body);
            notification.setChannel(Channel.IN_APP);
            notification.setData("requestId=" + savedRequest.getRequestId());
            notification.setIsRead(false);

            boolean isCritical = aiPrediction != null &&
                    ("HIGH".equalsIgnoreCase(aiPrediction.getPriority())
                            || "CRITICAL".equalsIgnoreCase(aiPrediction.getPriority()));

            notification.setIsCritical(isCritical);

            notificationRepository.save(notification);
        }

        return savedRequest;
    }

    @Override
    public List<ConversationRequest> getPendingRequests() {
        return conversationRequestRepository.findByStatus(ConversationRequestStatus.PENDING);
    }

    @Override
    public List<ConversationRequest> getRequestsByCitizen(Integer citizenId) {
        return conversationRequestRepository.findByCitizenId(citizenId);
    }

    @Override
    public List<ConversationRequest> getRequestsByAdmin(Integer adminId) {
        return conversationRequestRepository.findByAdminId(adminId);
    }

    @Override
    public ConversationRequest acceptRequest(Integer requestId, Integer adminId) {
        ConversationRequest request = conversationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        if (request.getStatus() != ConversationRequestStatus.PENDING) {
            throw new RuntimeException("This request has already been processed.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        User citizen = userRepository.findById(request.getCitizenId())
                .orElseThrow(() -> new RuntimeException("Citizen not found with id: " + request.getCitizenId()));

        Conversation conversation = new Conversation();
        conversation.setType(ConvoType.DIRECT);
        conversation.setRelatedEntityType("CONVERSATION_REQUEST");
        conversation.setRelatedEntityId(request.getRequestId());
        conversation.setParticipantIds(List.of(request.getCitizenId(), adminId));
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setLastMessageAt(LocalDateTime.now());

        Conversation savedConversation = conversationRepository.save(conversation);

        request.setAdminId(adminId);
        request.setConversationId(savedConversation.getConversationId());
        request.setStatus(ConversationRequestStatus.ACCEPTED);
        request.setProcessedAt(LocalDateTime.now());

        ConversationRequest updatedRequest = conversationRequestRepository.save(request);

        Notification notification = new Notification();
        notification.setRecipient(citizen);
        notification.setType(NotifType.CHAT);
        notification.setTitle("Demande acceptée");
        notification.setBody("Votre demande de discussion a été acceptée par un administrateur.");
        notification.setChannel(Channel.IN_APP);
        notification.setData("conversationId=" + savedConversation.getConversationId());
        notification.setIsRead(false);
        notification.setIsCritical(false);

        notificationRepository.save(notification);

        return updatedRequest;
    }

    @Override
    public ConversationRequest rejectRequest(Integer requestId, Integer adminId) {
        ConversationRequest request = conversationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        if (request.getStatus() != ConversationRequestStatus.PENDING) {
            throw new RuntimeException("This request has already been processed.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        User citizen = userRepository.findById(request.getCitizenId())
                .orElseThrow(() -> new RuntimeException("Citizen not found with id: " + request.getCitizenId()));

        request.setAdminId(adminId);
        request.setStatus(ConversationRequestStatus.REJECTED);
        request.setProcessedAt(LocalDateTime.now());

        ConversationRequest updatedRequest = conversationRequestRepository.save(request);

        Notification notification = new Notification();
        notification.setRecipient(citizen);
        notification.setType(NotifType.SYSTEM);
        notification.setTitle("Demande refusée");
        notification.setBody("Votre demande de discussion a été refusée.");
        notification.setChannel(Channel.IN_APP);
        notification.setData("requestId=" + updatedRequest.getRequestId());
        notification.setIsRead(false);
        notification.setIsCritical(false);

        notificationRepository.save(notification);

        return updatedRequest;
    }

    private List<User> findAdmins() {
        List<User> allUsers = userRepository.findAll();
        List<User> admins = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getRole() != null && isAdminRole(user.getRole())) {
                admins.add(user);
            }
        }

        return admins;
    }

    private boolean isAdminRole(UserRole role) {
        return role == UserRole.PLATFORM_ADMIN
                || role == UserRole.ORG_ADMIN
                || role == UserRole.AGENCY_ADMIN;
    }
    @Override
    public AiPredictionResponseDTO predictRequest(DiscussionRequestCreateDTO dto) {
        return aiPredictionService.predict(dto.getSubject(), dto.getDescription());
    }
}