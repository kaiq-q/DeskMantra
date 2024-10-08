package com.example.application.services;

import com.example.application.data.Repositories.TicketInteractionsRepository;
import com.example.application.data.TicketInteractions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketInteractionsService {

    private final TicketInteractionsRepository interactionsRepository;

    public TicketInteractionsService(TicketInteractionsRepository interactionsRepository) {
        this.interactionsRepository = interactionsRepository;
    }

    public List<TicketInteractions> findAllInteractionsInATicket(Integer idTicket){
        if (idTicket == 0){
            System.err.println("Ticket Id was not properly provided!");
            return null;
        }

        List<TicketInteractions> interactions = new ArrayList<>();
        interactionsRepository.findAllInteractionsInATicket(idTicket).forEach(interactions::add);
        return interactions;

    }

    public void saveInteraction(TicketInteractions ticketInteraction){
        interactionsRepository.save(ticketInteraction);
    }

    public void deleteAllInteractionsInATicket(Integer idTicket){
        interactionsRepository.deleteAllInteractionsInATicket(idTicket);
    }
}
