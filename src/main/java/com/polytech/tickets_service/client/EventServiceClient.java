package com.polytech.tickets_service.client;

import com.polytech.tickets_service.dto.EventResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

// "eventy-events-service" est le nom exact du service déclaré dans Eureka
@FeignClient(name = "eventy-events-service")
public interface EventServiceClient {

    @GetMapping("/events/{id}")
    EventResponseDto getEventById(@PathVariable("id") UUID id);
}