package com.example.application.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TicketInteractions", schema = "KaiqueTraining")
public class TicketInteractions {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDateTime dateInteraction;

    public TicketInteractions() {
        this.dateInteraction = LocalDateTime.now();
    }

    public LocalDateTime getDateInteraction() {
        return dateInteraction;
    }

    public void setDateInteraction(LocalDateTime dateInteraction) {
        this.dateInteraction = dateInteraction;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
