package com.pato.scheduler;

import com.pato.repository.ITicketRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class TicketCleanUpScheduler {

    private final ITicketRepository ticketRepository;

    public TicketCleanUpScheduler(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void eliminarTicketsViejos() {
        LocalDateTime limite = LocalDateTime.now().minusDays(15);
        ticketRepository.deleteByFechaHoraEntradaBefore(limite);
    }
}
