package com.example.application.views.Ticket;

import com.example.application.data.Ticket;

import com.example.application.data.TicketInteractions;
import com.example.application.data.Users;
import com.example.application.services.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;


@PageTitle("Tickets")
@Route(value = "tickets", layout = MainLayout.class)
public class TicketView extends VerticalLayout {

    Grid<Ticket> grid = new Grid<>(Ticket.class);
    TextField uniqueFilter = new TextField();
    TicketForm form;

    TicketService ticketService;
    UserService   userService;
    TicketStatusService ticketStatusService;
    TicketPriorityService ticketPriorityService;
    TicketInteractionsService ticketInteractionsService;

    public TicketView(TicketService ticketService,
                      TicketStatusService ticketStatusService,
                      TicketPriorityService ticketPriorityService,
                      UserService userService,
                      TicketInteractionsService ticketInteractionsService ) {

        this.ticketService = ticketService;
        this.userService = userService;
        this.ticketStatusService = ticketStatusService;
        this.ticketPriorityService = ticketPriorityService;
        this.ticketInteractionsService = ticketInteractionsService;

        addClassName("tickets-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getCotent());
        updateList();
        closeEditor();

    }

    private HorizontalLayout getToolbar(){
       uniqueFilter.setPlaceholder("Type you search...");
       uniqueFilter.setClearButtonVisible(true);
       uniqueFilter.setValueChangeMode(ValueChangeMode.LAZY);
       uniqueFilter.addValueChangeListener(e -> updateList());

       Button addNewTicket = new Button("Create new ticket");
       addNewTicket.addClickListener(clickEvent -> addNewTicket());

        return new HorizontalLayout(uniqueFilter, addNewTicket);
   }

    private void updateList(){
        grid.setItems(ticketService.findAllTickets(uniqueFilter.getValue()));
    }
    private void configureGrid(){

        grid.addClassNames("tickets-grid");
        grid.setSizeFull();
        grid.setColumns("title");
        grid.addColumn(ticketP ->ticketP.getPriority().getPriority()).setHeader("Priority");
        grid.addColumn(ticketS -> ticketS.getStatus().getStatus()).setHeader("Status");
        grid.addColumn(ticketUser -> ticketUser.getUser().getName()).setHeader("User");
        grid.addColumn(ticket -> {
            if (ticket.getTechnician() != null) {
                return ticket.getTechnician().getName();
            } else {
                return "No Technician"; // or any placeholder text you want
            }
        }).setHeader("Technician");


        grid.asSingleSelect().addValueChangeListener(event -> editTickets(event.getValue()));

    }
    private void configureForm(){

        form = new TicketForm(ticketStatusService.findAllTicketStatus(""),
                ticketPriorityService.findAllTicketPriority(""), userService.findAllUsers(""));
        form.setWidth("50em");

        form.addSaveListener(this::saveTicket);
        form.addDeleteListener(this::deleteTicket);
        form.addCloseListener(e -> closeEditor());


    }

    private void closeEditor(){
        form.setTicket(null);
        form.setVisible(false);
        form.getTempInteractions().clear();
        removeClassName("editing");
    }

    private void saveTicket(TicketForm.TicketFormEvent.SaveEvent saveEvent){

        Ticket ticket = ticketService.saveTicket(saveEvent.getTicket());

        List<TicketInteractions> ticketInteractions = form.getTempInteractions();

        for (TicketInteractions interaction : ticketInteractions){
            interaction.setTicket(ticket);
            ticketInteractionsService.saveInteraction(interaction);
        }


        updateList();
        closeEditor();

    }
    private void deleteTicket(TicketForm.TicketFormEvent.DeleteEvent deleteEvent){

        ticketInteractionsService.deleteAllInteractionsInATicket(deleteEvent.getTicket().getId());

        ticketService.deleteTicket(deleteEvent.getTicket());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getCotent(){

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2,grid);
        content.setFlexGrow(1,form);
        content.addClassName("content");
        content.setSizeFull();
        return content;

    }

    public void editTickets(Ticket ticket){

        if (ticket == null){
            closeEditor();
        }else{
            form.setTicket(ticket);
            if (ticket.getId() != null){
                form.setDumpInteractions(ticketInteractionsService.findAllInteractionsInATicket(ticket.getId()));
            }
            form.setVisible(true);
            addClassName("editing");
        }

    }

    private void addNewTicket(){

        grid.asSingleSelect().clear();

        Ticket ticket = new Ticket();

        ticket.setPriority(ticketPriorityService.searchDefault());
        ticket.setStatus(ticketStatusService.searchDefault());

        //This must change
        Users user = userService.findById(10);

        form.setDumpInteractions(new ArrayList<>());

        ticket.setUser(user);

        editTickets(ticket);

    }


}
