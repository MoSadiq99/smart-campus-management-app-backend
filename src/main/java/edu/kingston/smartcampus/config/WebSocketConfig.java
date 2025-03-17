package edu.kingston.smartcampus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOrigins("http://localhost:4200")
//                .addInterceptors(new HttpSessionHandshakeInterceptor() {
//                    @Override
//                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//                        // Allow SockJS handshake requests (e.g., /ws/info) without strict token check
//                        String path = request.getURI().getPath();
//                        if (path.contains("/ws/info") || path.contains("/xhr") || path.contains("/jsonp")) {
//                            return true; // Permit SockJS handshake requests
//                        }
//
//                        // Check Authorization header for WebSocket/STOMP connection
//                        String authHeader = request.getHeaders().getFirst("Authorization");
//                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                            String token = authHeader.substring(7);
//                            attributes.put("token", token);
//                            return true; // Allow handshake with valid token
//                        }
//                        return false; // Reject if no valid token for non-SockJS paths
//                    }
//                })
//                .withSockJS();
//    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String authHeader = accessor.getFirstNativeHeader("Authorization");
//                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                        String token = authHeader.substring(7);
//                        // Validate token here (e.g., using JwtFilter or a custom service)
//                        // For now, just log it
//                        System.out.println("STOMP CONNECT token: " + token);
//                        return message; // Allow connection
//                    }
//                    throw new SecurityException("Invalid or missing token");
//                }
//                return message;
//            }
//        });
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }
}