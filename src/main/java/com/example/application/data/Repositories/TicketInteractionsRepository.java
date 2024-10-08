package com.example.application.data.Repositories;

import com.example.application.data.TicketInteractions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TicketInteractionsRepository extends CrudRepository<TicketInteractions, Integer> {

    /**
     * Retrieves all interactions associated with a specific ticket.
     *
     * @param ticketId the identifier of the ticket whose interactions are to be retrieved
     * @return a list of TicketInteractions associated with the specified ticket
     */
    @Query("select interactions from TicketInteractions interactions "+
           "where interactions.ticket.id = :ticketId" )
    List<TicketInteractions> findAllInteractionsInATicket(@Param("ticketId") Integer ticketId);

/**
 * Modifies the database by deleting all interactions associated with a specific ticket.
 *
 * @param ticketId the identifier of the ticket whose interactions are to be deleted
 */
    @Modifying
    @Query("delete from TicketInteractions interactions "+
           "where interactions.ticket.id = :ticketId" )
    void deleteAllInteractionsInATicket(@Param("ticketId") Integer ticketId);
}
