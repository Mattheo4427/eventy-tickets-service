package com.polytech.tickets_service.model;

import com.polytech.tickets_service.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_id")
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId; // Référence vers Event Service

    @Column(name = "vendor_id", nullable = false)
    private UUID vendorId; // L'utilisateur qui vend (ou l'orga)

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "sale_price")
    private Double salePrice; // Peut être différent si revente

    private String section;
    private Integer row;
    
    // Utilisation de String au lieu de char pour plus de flexibilité (ex: "12A")
    // Si vous tenez absolument au char : private Character seat;
    private String seat; 

    private String barcode; // Généré lors de la création
    
    @Column(name = "qr_code")
    private String qrCode;  // URL ou contenu du QR

    @CreationTimestamp
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.AVAILABLE;

    // --- AJOUT ---
    @CreationTimestamp // Génère la date automatiquement côté Java/Hibernate si besoin
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;
}