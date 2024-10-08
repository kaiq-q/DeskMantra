package com.example.application.data.Repositories;

import com.example.application.data.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<Ticket, Integer> {



}
