/**** package tn.esprit.disasteraidbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tn.esprit.disasteraidbackend.security.JwtAuthenticationFilter;

/**
 * JWT-based stateless security.
 *
 * Role matrix:
 *   ADMIN    → full access (GET, POST, PUT, DELETE, PATCH)
 *   OPERATOR → GET + POST + PUT + PATCH  (no DELETE)
 *   VIEWER   → GET only
 *
@Configuration
@EnableMethodSecurity           // enables @PreAuthorize on service/controller methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()           // WebSocket handshake
                        .requestMatchers("/actuator/**").permitAll()

                        // DELETE requires ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        // POST / PUT / PATCH require OPERATOR or ADMIN
                        .requestMatchers(HttpMethod.POST,  "/api/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers(HttpMethod.PUT,   "/api/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "OPERATOR")

                        // GET is available to all authenticated users
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "OPERATOR", "VIEWER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

    until we have users
 */

package tn.esprit.disasteraidbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tn.esprit.disasteraidbackend.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        // ============================================================
                        // 🔧 DEVELOPMENT MODE: permit all requests to your endpoints
                        // TODO: REMOVE THESE LINES WHEN USER SYSTEM IS READY
                        // ============================================================
                        .requestMatchers(
                                "/api/alerts/**",
                                "/api/sensors/**",
                                "/api/geofence-zones/**",
                                "/api/evacuation-routes/**",
                                "/api/route-detail/**"
                        ).permitAll()
                        // ============================================================

                        // Keep original security for other endpoints (if any)
                        // 👇 If you have other APIs that should stay protected, list them here
                        // .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Default: all other requests require authentication
                        .anyRequest().authenticated()
                )
                // Add JWT filter – it will still run, but permitAll endpoints skip auth check
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}