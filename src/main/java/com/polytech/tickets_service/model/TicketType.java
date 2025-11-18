package com.polytech.tickets_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "ticket_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_type_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String label; // Ex: "VIP", "Standard", "Early Bird"
}