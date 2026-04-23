package tn.esprit.disasteraidbackend.modules.communication.Entities;

public enum VoiceCallSignalType {
    CALL_INIT,
    CALL_RINGING,
    CALL_ACCEPTED,
    CALL_REJECTED,
    WEBRTC_OFFER,
    WEBRTC_ANSWER,
    ICE_CANDIDATE,
    CALL_ENDED
}
