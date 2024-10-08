package com.example.application.data.Repositories;


import com.example.application.data.TicketStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TicketStatusRepository extends CrudRepository<TicketStatus, Integer> {

    @Query("select status from TicketStatus status "+
           "where status.isDefault = true")
    TicketStatus statusDefault();
}
