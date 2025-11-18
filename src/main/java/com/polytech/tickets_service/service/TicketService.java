package com.polytech.tickets_service.service;

import com.polytech.tickets_service.dto.TicketRequestDto;
import com.polytech.tickets_service.model.Ticket;
import com.polytech.tickets_service.model.TicketType;
import com.polytech.tickets_service.model.enums.TicketStatus;
import com.polytech.tickets_service.repository.TicketRepository;
import com.polytech.tickets_service.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;

    /**
     * Crée un nouveau ticket pour la vente.
     */
    @Transactional
    public Ticket createTicket(TicketRequestDto request, String vendorId) {
        // 1. Vérifier ou récupérer le type de ticket
        TicketType type = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Type de ticket introuvable"));

        // 2. Construire l'objet Ticket
        Ticket ticket = Ticket.builder()
                .eventId(request.getEventId())
                .vendorId(UUID.fromString(vendorId))
                .ticketType(type)
                .originalPrice(request.getOriginalPrice())
                .salePrice(request.getSalePrice())
                .section(request.getSection())
                .row(request.getRow())
                .seat(request.getSeat())
                .status(TicketStatus.AVAILABLE)
                // Génération simple de codes (à remplacer par une logique métier plus complexe si besoin)
                .barcode(UUID.randomUUID().toString()) 
                .qrCode("QR-" + UUID.randomUUID().toString())
                .build();

        return ticketRepository.save(ticket);
    }

    /**
     * Marque un ticket comme VENDU.
     * Cette méthode sera appelée lors de la finalisation d'une commande (Transaction Service).
     */
    @Transactional
    public void buyTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        if (ticket.getStatus() != TicketStatus.AVAILABLE) {
            throw new IllegalStateException("Ce ticket n'est plus disponible à la vente");
        }

        ticket.setStatus(TicketStatus.SOLD);
        ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByEvent(UUID eventId) {
        return ticketRepository.findByEventId(eventId);
    }
    
    public List<Ticket> getAvailableTicketsByEvent(UUID eventId) {
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.AVAILABLE);
    }
}