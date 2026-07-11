package com.example.supportdesk.config;

import com.example.supportdesk.model.Ticket;
import com.example.supportdesk.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class TicketDataSeeder {

    @Bean
    CommandLineRunner seedTickets(TicketRepository ticketRepository) {
        return args -> {
            if (ticketRepository.count() > 0) {
                return;
            }

            ticketRepository.save(new Ticket(
                    "Login page not loading",
                    "Users report the login page shows a blank screen on Chrome.",
                    "Bug",
                    "High",
                    "Open",
                    "ali@example.com",
                    LocalDateTime.now().minusDays(3)
            ));

            ticketRepository.save(new Ticket(
                    "Add dark mode",
                    "Request to add dark mode theme for the dashboard.",
                    "Feature Request",
                    "Medium",
                    "Open",
                    "sit@example.com",
                    LocalDateTime.now().minusDays(1)
            ));

            ticketRepository.save(new Ticket(
                    "Update user guide",
                    "User guide needs to reflect the latest UI changes.",
                    "Documentation",
                    "Low",
                    "Closed",
                    "mutu@example.com",
                    LocalDateTime.now().minusDays(7)
            ));
        };
    }
}
