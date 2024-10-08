package com.example.application.data.Repositories;

import com.example.application.data.TicketPriority;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TicketPriorityRepository extends CrudRepository<TicketPriority, Integer> {

    @Query("select priority from TicketPriority priority "+
           "where priority.isDefault = true")
    TicketPriority priorityDefault();
}
