package com.polytech.tickets_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class EventResponseDto {
    private UUID id;
    private String name;
    private String status; // Ex: ACTIVE, CANCELLED, FULL
    // On n'a pas besoin de tout mapper, juste ce qui sert à la validation
}