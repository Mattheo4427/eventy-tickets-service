package com.polytech.tickets_service.service;

import com.polytech.tickets_service.dto.TicketRequestDto;
import com.polytech.tickets_service.dto.TicketResponseDto;
import com.polytech.tickets_service.model.Ticket;
import com.polytech.tickets_service.model.TicketType;
import com.polytech.tickets_service.model.enums.TicketStatus;
import com.polytech.tickets_service.repository.TicketRepository;
import com.polytech.tickets_service.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.polytech.tickets_service.client.EventServiceClient; // <--- Import
import com.polytech.tickets_service.dto.EventResponseDto;      // <--- Import
import feign.FeignException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventServiceClient eventServiceClient;

    /**
     * Crée un nouveau ticket pour la vente.
     */
    @Transactional
    public Ticket createTicket(TicketRequestDto request, String vendorId) {
        // 1. Vérifier l'existence et le statut de l'événement via le microservice Events
        EventResponseDto event;
        try {
            event = eventServiceClient.getEventById(request.getEventId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Event not found with ID: " + request.getEventId());
        }

        // Vérification optionnelle du statut
        if (!"ACTIVE".equalsIgnoreCase(event.getStatus())) {
            throw new IllegalStateException("Cannot create ticket for an event that is not ACTIVE (Current status: " + event.getStatus() + ")");
        }

        // 2. Récupérer le type de ticket (interne au service Ticket)
        TicketType ticketType = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket Type not found"));

        // 3. Création du ticket
        Ticket ticket = Ticket.builder()
                .eventId(request.getEventId())
                .vendorId(UUID.fromString(vendorId))
                .ticketType(ticketType)
                .originalPrice(request.getOriginalPrice())
                .salePrice(request.getSalePrice())
                .section(request.getSection())
                .row(request.getRow())
                .seat(request.getSeat())
                .saleDate(LocalDate.now().atStartOfDay())
                .status(TicketStatus.AVAILABLE)
                .build();

        // Génération de codes factices pour le MVP
        ticket.setBarcode(UUID.randomUUID().toString());
        ticket.setQrCode("QR-" + ticket.getId());

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

    public List<TicketType> getTicketTypes(){
        return ticketTypeRepository.findAll();
    }

    public List<TicketResponseDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private TicketResponseDto mapToResponseDto(Ticket ticket) {
        return TicketResponseDto.builder()
                .id(ticket.getId())
                .eventId(ticket.getEventId())
                .vendorId(ticket.getVendorId())
                .sellerName("Vendeur " + ticket.getVendorId().toString().substring(0, 5)) // Placeholder, le front fera la jointure ou on appellera UserClient plus tard
                .typeLabel(ticket.getTicketType().getLabel())
                .originalPrice(ticket.getOriginalPrice())
                .salePrice(ticket.getSalePrice())
                .section(ticket.getSection())
                .row(ticket.getRow())
                .seat(ticket.getSeat())
                .status(ticket.getStatus())
                .barcode(ticket.getBarcode())
                .qrCode(ticket.getQrCode())
                .build();
    }

    public Optional<Ticket> getTicketById(UUID id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByVendor(UUID vendorId) {
        return ticketRepository.findByVendorId(vendorId);
    }


}