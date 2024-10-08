package com.example.application.services;

import com.example.application.data.Repositories.TicketPriorityRepository;
import com.example.application.data.TicketPriority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TicketPriorityService {

    private final TicketPriorityRepository ticketPriorityRepository;

    public TicketPriorityService(TicketPriorityRepository ticketPriorityRepository) {
        this.ticketPriorityRepository = ticketPriorityRepository;
    }

    public List<TicketPriority> findAllTicketPriority(String filter){
        if (filter == null || filter.isEmpty()){
            List<TicketPriority> listPriority = new ArrayList<>();
            ticketPriorityRepository.findAll().forEach(listPriority::add);
            return listPriority;
        }else{
            return null;
        }
    }

    public void deleteTicketPriority(TicketPriority ticketPriority){
        ticketPriorityRepository.delete(ticketPriority);
    }

    public void saveTicketPriority(TicketPriority ticketPriority){

        TicketPriority currentDefault = ticketPriorityRepository.priorityDefault();

        if (currentDefault != null && !currentDefault.getId().equals(ticketPriority.getId()) && ticketPriority.isDefault()) {
            throw new IllegalArgumentException("Only one priority can be default ❌");
        }

        if (ticketPriority.getPriority() == null || ticketPriority.getPriority().isEmpty()){
            throw new IllegalArgumentException("Priority name is empty ❌");

        }

        if (ticketPriority.getDescription() == null || ticketPriority.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty ❌");
        }

        ticketPriorityRepository.save(ticketPriority);

    }

    public TicketPriority searchDefault(){
        return ticketPriorityRepository.priorityDefault();
    }


}
