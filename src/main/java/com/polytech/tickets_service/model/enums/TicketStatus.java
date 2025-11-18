package com.polytech.tickets_service.model.enums;

public enum TicketStatus {
    AVAILABLE,  // Mis en vente
    RESERVED,   // Dans un panier (optionnel)
    SOLD,       // Vendu
    CANCELED    // Annulé par le vendeur ou l'event
}