package com.polytech.tickets_service.repository;

import com.polytech.tickets_service.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
    Optional<TicketType> findByLabel(String label);
}