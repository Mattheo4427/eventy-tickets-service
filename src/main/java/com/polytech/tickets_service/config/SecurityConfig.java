package com.polytech.tickets_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Injection des variables pour construire l'URL JWK interne
    @Value("${keycloak.server-url:http://keycloak:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:eventy-realm}")
    private String realm;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Routes Admin (si vous voulez sécuriser spécifiquement)
                        .requestMatchers("/tickets/admin/**").hasRole("ADMIN")

                        .requestMatchers("/tickets/**").authenticated()
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder()) // Utilisation du décodeur permissif
                        )
                );

        return http.build();
    }

    /**
     * Décodeur JWT qui récupère les clés via le réseau Docker interne
     * mais ignore la validation stricte de l'URL de l'émetteur.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = String.format("%s/realms/%s/protocol/openid-connect/certs", keycloakServerUrl, realm);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Convertisseur pour extraire les rôles (realm_access + app_role)
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            // Rôles Keycloak standards
            if (jwt.hasClaim("realm_access")) {
                Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
                Object roles = realmAccess.get("roles");
                if (roles instanceof Collection<?> rolesCollection) {
                    List<GrantedAuthority> realmRoles = rolesCollection.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                            .collect(Collectors.toList());
                    authorities.addAll(realmRoles);
                }
            }

            // Attribut personnalisé 'app_role'
            if (jwt.hasClaim("app_role")) {
                String appRole = jwt.getClaimAsString("app_role");
                if (appRole != null && !appRole.trim().isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + appRole.toUpperCase()));
                }
            }
            return authorities;
        });
        return converter;
    }
}