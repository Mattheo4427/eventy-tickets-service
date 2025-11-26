package com.polytech.tickets_service.service;

import com.polytech.tickets_service.event.TicketSoldEvent;
import com.polytech.tickets_service.model.Ticket;
import com.polytech.tickets_service.model.enums.TicketStatus;
import com.polytech.tickets_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final TicketRepository ticketRepository;

    @KafkaListener(topics = "ticket-sold", groupId = "tickets-service-group")
    @Transactional
    public void handleTicketSold(TicketSoldEvent event) {
        log.info("Received TicketSoldEvent for ticket: {}", event.getTicketId());

        ticketRepository.findById(event.getTicketId()).ifPresentOrElse(
                ticket -> {
                    // Idempotence : Si déjà vendu, on ne fait rien (évite les erreurs de lock inutiles)
                    if (ticket.getStatus() != TicketStatus.SOLD) {
                        ticket.setStatus(TicketStatus.SOLD);
                        ticketRepository.save(ticket);
                        log.info("Ticket {} passé à SOLD via Kafka", ticket.getId());
                    }
                },
                () -> log.warn("Kafka: Ticket introuvable {}", event.getTicketId()) // Warn au lieu d'Error pour éviter le spam si suppression
        );
    }
}