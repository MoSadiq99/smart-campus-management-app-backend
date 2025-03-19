package edu.kingston.smartcampus.config;

import edu.kingston.smartcampus.security.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker // Enable WebSocket support
public class BeansConfig implements WebSocketMessageBrokerConfigurer {

    private final MyUserDetailsService userDetailsService;

    public BeansConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (for JWT Authorization header)
        config.setAllowCredentials(true);

        // Specify allowed origin (Angular frontend)
        config.addAllowedOrigin("http://localhost:4200");

        // Allowed headers (expanded for flexibility)
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,  // For JWT Bearer token
                HttpHeaders.CONTENT_TYPE,   // For POST/PUT requests
                HttpHeaders.ACCEPT,         // For response type negotiation
                HttpHeaders.ORIGIN,         // Required for CORS
                "Cache-Control",            // Optional: Common header
                "X-Requested-With"          // Optional: Used by some frameworks
        ));

        // Allowed methods (covers typical REST operations)
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Optional: Expose headers if needed (e.g., for custom response headers)
        config.setExposedHeaders(List.of(
                HttpHeaders.CONTENT_DISPOSITION // For file download filename
        ));

        // Apply to all paths
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
