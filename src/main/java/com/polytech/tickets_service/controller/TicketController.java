package com.polytech.tickets_service.controller;

import com.polytech.tickets_service.dto.TicketRequestDto;
import com.polytech.tickets_service.dto.TicketResponseDto;
import com.polytech.tickets_service.model.Ticket;
import com.polytech.tickets_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Endpoint pour mettre un ticket en vente.
     * Seul un utilisateur authentifié peut le faire.
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(
            @RequestBody TicketRequestDto request,
            @AuthenticationPrincipal Jwt principal) {
        
        // Récupération de l'ID utilisateur depuis le token Keycloak
        String vendorId = principal.getSubject();
        
        Ticket createdTicket = ticketService.createTicket(request, vendorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    /**
     * Récupérer tous les tickets disponibles pour un événement donné.
     * Public ou Authentifié.
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getTicketsByEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ticketService.getAvailableTicketsByEvent(eventId));
    }

    /**
     * Acheter un ticket (Change le statut).
     * Appelé potentiellement par le front ou le Transaction Service.
     */
    @PostMapping("/{id}/buy")
    public ResponseEntity<Void> buyTicket(@PathVariable UUID id) {
        ticketService.buyTicket(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
        // TODO: Ajouter @PreAuthorize("hasRole('ADMIN')") quand la sécurité sera active sur ce service
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}