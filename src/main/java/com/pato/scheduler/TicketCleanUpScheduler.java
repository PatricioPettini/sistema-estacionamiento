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

    // Corre una vez al día a las 3 AM
    @Scheduled(cron = "0 0 3 * * ?")
    public void eliminarTicketsViejos() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        ticketRepository.deleteByFechaHoraEntradaBefore(limite);
        System.out.println("🗑️ Tickets anteriores a " + limite + " eliminados");
    }
}
