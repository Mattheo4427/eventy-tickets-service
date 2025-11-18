package com.polytech.tickets_service.repository;

import com.polytech.tickets_service.model.Ticket;
import com.polytech.tickets_service.model.TicketType;
import com.polytech.tickets_service.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    
    // Récupérer tous les tickets d'un événement
    List<Ticket> findByEventId(UUID eventId);

    // Récupérer les tickets disponibles pour un événement
    List<Ticket> findByEventIdAndStatus(UUID eventId, TicketStatus status);

    // Récupérer les tickets mis en vente par un utilisateur spécifique
    List<Ticket> findByVendorId(UUID vendorId);
}