package com.example.application.views.Ticket;

import com.example.application.data.TicketPriority;
import com.example.application.services.TicketPriorityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.awt.*;

@PageTitle("Ticket Priority")
@Route(value = "tickets/priority", layout = MainLayout.class)

public class TicketPriorityView extends VerticalLayout {

    TextField uniqueFilter = new TextField();
    Grid<TicketPriority> grid = new Grid<>(TicketPriority.class);

    TicketPriorityForm form;
    TicketPriorityService priorityService;

    public TicketPriorityView(TicketPriorityService priorityService){
        this.priorityService = priorityService;
        addClassName("ticket-priority-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();

    }

    private HorizontalLayout getContent(){
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private Component getToolbar() {

        uniqueFilter.setPlaceholder("Type your search...");
        uniqueFilter.setClearButtonVisible(true);
        uniqueFilter.setValueChangeMode(ValueChangeMode.LAZY);
        uniqueFilter.addValueChangeListener(event -> updateList());

        Button addPriority = new Button("Add new priority");
        addPriority.addClickListener(click -> addNewPriority());

        HorizontalLayout toolbar = new HorizontalLayout(uniqueFilter, addPriority);
        toolbar.addClassName("toolbar");

        return toolbar;

    }

    private void addNewPriority() {

        grid.asSingleSelect().clear();
        editPriority(new TicketPriority());

    }

    private void configureGrid() {

        grid.addClassNames("ticke-priority-grid");
        grid.setSizeFull();
        grid.setColumns("priority", "description");
        grid.getColumns().forEach(ticketPriorityColumn -> ticketPriorityColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editPriority(event.getValue()));

    }

    private void editPriority(TicketPriority priority) {

        if (priority == null){
            closeEditor();
        }else{
            form.setPriority(priority);
            form.setVisible(true);
            addClassName("editing");
        }

    }

    private void configureForm(){
        form = new TicketPriorityForm(priorityService.findAllTicketPriority(""));
        form.setWidth("40em");

        form.addSaveListener(this::savePriority);
        form.addDeleteListener(this::deletePriority);
        form.addCloseListener(e -> closeEditor());
    }

    private void deletePriority(TicketPriorityForm.PriotiryFormEvent.DeleteEvent deleteEvent){
        priorityService.deleteTicketPriority(deleteEvent.getPriority());
        updateList();
        closeEditor();
    }

    private void savePriority(TicketPriorityForm.PriotiryFormEvent.SaveEvent saveEvent){

        try {
            priorityService.saveTicketPriority(saveEvent.getPriority());
            Notification.show("Priority saved successfully ✔", 3000, Notification.Position.MIDDLE);
        } catch (IllegalArgumentException e) {
            // Catch and display the specific validation error
            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
        }

        
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(priorityService.findAllTicketPriority(uniqueFilter.getValue()));
    }

    private void closeEditor() {
        form.setPriority(null);
        form.setVisible(false);
        removeClassName("editing");

    }

}
