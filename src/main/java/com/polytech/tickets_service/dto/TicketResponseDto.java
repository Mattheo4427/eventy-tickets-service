package com.polytech.tickets_service.dto;

import com.polytech.tickets_service.model.enums.TicketStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TicketResponseDto {
    private UUID id;
    private UUID eventId;
    private UUID vendorId; // correspond au sellerId du front
    private String sellerName; // Sera enrichi par le front via User Service ou ici via Feign
    private String typeLabel;
    private Double originalPrice;
    private Double salePrice;
    private String section; // Changé en Integer pour matcher l'entité, ou String si vous préférez
    private Integer row;
    private String seat;
    private TicketStatus status;
    // Ajout de champs optionnels pour l'affichage
    private String barcode;
    private String qrCode;
}