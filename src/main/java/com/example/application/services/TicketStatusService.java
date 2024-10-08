package com.example.application.services;

import com.example.application.data.Repositories.TicketStatusRepository;
import com.example.application.data.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;


    public TicketStatusService(TicketStatusRepository ticketStatusRepository) {
        this.ticketStatusRepository = ticketStatusRepository;
    }


    public List<TicketStatus> findAllTicketStatus(String filter){

        if (filter == null || filter.isEmpty()){

            List<TicketStatus> listStatus = new ArrayList<>();
            ticketStatusRepository.findAll().forEach(listStatus::add);
            return listStatus;
        }else{
            return null;
        }

    }

    public void deleteTicketStatus(TicketStatus ticketStatus){
        ticketStatusRepository.delete(ticketStatus);
    }

    public void saveTicketStatus(TicketStatus ticketStatus){

        TicketStatus currentDefault = ticketStatusRepository.statusDefault();

        if (currentDefault != null && !currentDefault.getId().equals(ticketStatus.getId()) && ticketStatus.isDefault()){
            throw new IllegalArgumentException("Only one status can be default ❌");
        }

        if (ticketStatus.getDescription().isEmpty() || ticketStatus.getDescription() == null){
            throw new IllegalArgumentException("Description cannot not be empty ❌");
        }

        if (ticketStatus.getStatus().isEmpty() || ticketStatus.getStatus() == null){
            throw new IllegalArgumentException("Status title cannot not be empty ❌");
        }

        ticketStatusRepository.save(ticketStatus);
    }

    public TicketStatus searchDefault(){
        return ticketStatusRepository.statusDefault();
    }
}
