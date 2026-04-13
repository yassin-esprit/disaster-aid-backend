package tn.esprit.disasteraidbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket / STOMP configuration.
 *
 * Frontend connects to: ws://localhost:8090/ws
 * Subscribe to topics:  /topic/alerts, /topic/evacuation
 *
 * Example Angular usage:
 *   const client = new Client({ brokerURL: 'ws://localhost:8090/ws' });
 *   client.subscribe('/topic/alerts', msg => console.log(msg.body));
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // In-memory broker for /topic/* destinations
        config.enableSimpleBroker("/topic");
        // Prefix for messages sent FROM the client TO the server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")   // tighten in production
                .withSockJS();                   // fallback for browsers without WS
    }
}