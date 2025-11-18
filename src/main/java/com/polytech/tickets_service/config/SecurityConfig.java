package com.polytech.tickets_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF (inutile pour une API Stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Pas de session serveur (Stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Règles d'accès
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll() // Métriques publiques
                .anyRequest().authenticated() // Tout le reste nécessite un token JWT valide
            )
            
            // Validation du Token JWT via Keycloak
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}