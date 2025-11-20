package com.polytech.tickets_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TicketRequestDto {
    private UUID eventId;
    private UUID ticketTypeId;
    private Double originalPrice;
    private Double salePrice;
    private Integer section;
    private Integer row;
    private String seat;
}