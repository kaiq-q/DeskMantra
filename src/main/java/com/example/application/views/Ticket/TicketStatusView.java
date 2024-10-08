package com.example.application.views.Ticket;

import com.example.application.data.TicketStatus;
import com.example.application.data.Users;
import com.example.application.services.PermissionsService;
import com.example.application.services.TicketStatusService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
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

@PageTitle("Ticket Status")
@Route(value = "tickets/status", layout = MainLayout.class)

public class TicketStatusView extends VerticalLayout {

        private static final String criteria =  "ROOT";

        TextField uniqueFilter = new TextField();
        Grid<TicketStatus> grid = new Grid<>(TicketStatus.class);
        TicketStatusForm form;
        TicketStatusService ticketStatusService;
        PermissionsService permissionsService;
        UserService usersService;



        public TicketStatusView(TicketStatusService ticketStatusService, PermissionsService permissionsService, UserService usersService) {
            this.ticketStatusService = ticketStatusService;
            this.permissionsService = permissionsService;
            this.usersService = usersService;

            addClassName("ticket-status-view");
            setSizeFull();
            configureGrid();
            configureForm();

            add(getToolbar(), getContent());
            updateList();
            closeEditor();

        }

        private void closeEditor() {

                form.setStatus(null);
                form.setVisible(false);
                removeClassName("editing");
        }

        private void saveStatus(TicketStatusForm.StatusFormEvent.SaveEvent saveEvent){

            try{
                ticketStatusService.saveTicketStatus(saveEvent.getStatus());
                Notification.show("Priority saved successfully ✔", 3000, Notification.Position.MIDDLE);

            }catch(IllegalArgumentException e){
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }

            updateList();
            closeEditor();

        }
        private void deleteStatus(TicketStatusForm.StatusFormEvent.DeleteEvent deleteEvent){
            ticketStatusService.deleteTicketStatus(deleteEvent.getStatus());
            updateList();
            closeEditor();
        }
        private void configureGrid(){

                grid.addClassNames("ticket-status-grid");
                grid.setSizeFull();
                grid.setColumns("status", "description");
                grid.getColumns().forEach(ticketStatusColumn -> ticketStatusColumn.setAutoWidth(true));

                grid.asSingleSelect().addValueChangeListener(event -> editStatus(event.getValue()));

        }
        private void editStatus(TicketStatus status){
            if (status == null){
                closeEditor();
            }else{

                Users currentUser = new Users();
                currentUser = usersService.findById(10);

                if (!currentUser.getRoles().stream().anyMatch(role ->
                        role.getPermissions().stream().anyMatch(permission ->
                                permission.getPermission().equals(criteria)))) {
                    form.save.setEnabled(false);
                    form.delete.setEnabled(false);
                }

                form.setStatus(status);
                form.setVisible(true);
                addClassName("editing");
            }
        }
        private HorizontalLayout getToolbar(){

            uniqueFilter.setPlaceholder("Type your search...");
            uniqueFilter.setClearButtonVisible(true);
            uniqueFilter.setValueChangeMode(ValueChangeMode.LAZY);
            uniqueFilter.addValueChangeListener(event -> updateList());

            Button addStatus = new Button("Add new status");
            addStatus.addClickListener(click -> addNewStatus());

            HorizontalLayout toolbar = new HorizontalLayout(uniqueFilter, addStatus);
            toolbar.addClassName("toolbar");

            return toolbar;
        }

        private void addNewStatus(){
            grid.asSingleSelect().clear();
            editStatus(new TicketStatus());
        }

        private HorizontalLayout getContent(){
            HorizontalLayout content = new HorizontalLayout(grid, form);
            content.setFlexGrow(2,grid);
            content.setFlexGrow(1, form);
            content.addClassName("content");
            content.setSizeFull();
            return content;
        }

        private void configureForm(){

            form = new TicketStatusForm(ticketStatusService.findAllTicketStatus(""));
            form.setWidth("40em");

            form.addSaveListener(this::saveStatus);
            form.addDeleteListener(this::deleteStatus);
            form.addCloseListener(e -> closeEditor());


        }

        private void updateList(){
            grid.setItems(ticketStatusService.findAllTicketStatus(uniqueFilter.getValue()));
        }

}
