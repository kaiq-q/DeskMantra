package com.example.application.services;

import com.example.application.data.Repositories.TicketRepository;
import com.example.application.data.Ticket;
import com.example.application.data.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAllTickets(String filter){

        if (filter == null || filter.isEmpty()){

            List<Ticket> ticketList = new ArrayList<>();
            ticketRepository.findAll().forEach(ticketList::add);
            return ticketList;
        }else {
            return null;
        }
    }

    public void deleteTicket(Ticket ticket){
        ticketRepository.delete(ticket);
    }

    public Ticket saveTicket(Ticket ticket){

        return ticketRepository.save(ticket);

    }

}
