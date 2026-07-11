package com.example.supportdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.supportdesk.model.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    // Custom query methods can be added here if needed
}
