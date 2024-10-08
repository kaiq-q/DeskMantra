package com.example.application.views.Ticket;

import com.example.application.configurationComponents.ButtonConfiguration;
import com.example.application.data.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TicketForm  extends FormLayout {

    BeanValidationBinder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);
    TextField title = new TextField("Title");
    ButtonConfiguration buttonConfiguration = new ButtonConfiguration();

    Button save   = buttonConfiguration.getSaveButton();
    Button delete = buttonConfiguration.getDeleteButton();
    Button close  = buttonConfiguration.getCloseButton();


    ComboBox<TicketStatus> status = new ComboBox<>("Status");
    ComboBox<TicketPriority> priority = new ComboBox<>("Priority");

    TextField userName = new TextField("User ");

    TextField technicianName = new TextField("Technician ");
    ComboBox<Users> technicianCombobox = new ComboBox<>("Technician ");

    Dialog dialogInteraction = new Dialog();
    Button okInteracion      =  buttonConfiguration.getSaveButton("Ok");
    Button cancelInteraction =  buttonConfiguration.getCloseButton("Cancel");

    private final List<MessageListItem> messageItems = new ArrayList<>();
    private final List<TicketInteractions>  tempInteractions = new ArrayList<>();
    private List<TicketInteractions>  dumpInteractions = new ArrayList<>();

    Dialog dialogTechnician  = new Dialog();
    Button okTechnician      = buttonConfiguration.getSaveButton("Ok");
    Button cancelTechnician  = buttonConfiguration.getCloseButton("Cancel");

    MessageList listInteractions = new MessageList();

    Button transferTo = new Button("Transfer To", event -> dialogTechnician.open());

    TextArea interaction = new TextArea("Type here");

    Button visualizeInteraction = new Button("Interactions", e -> dialogInteraction.open());

    //Constructor
    public TicketForm(List<TicketStatus> statusList, List<TicketPriority> priorityList, List<Users> usersList) {


        binder.bind(title, "title");
        title.setRequiredIndicatorVisible(true);

        binder.forField(userName)
                .bind(ticket -> ticket.getUser() != null ? ticket.getUser().getName() : "", null);

        binder.forField(technicianName)
                .bind(ticket -> ticket.getTechnician() != null ? ticket.getTechnician().getName() : "", null);

        userName.setReadOnly(true);
        technicianName.setReadOnly(true);

        status.setItems(statusList);
        status.setItemLabelGenerator(TicketStatus::getStatus);

        priority.setItems(priorityList);
        priority.setItemLabelGenerator(TicketPriority::getPriority);

        technicianCombobox.setItems(usersList);
        technicianCombobox.setItemLabelGenerator(Users::getName);

        binder.forField(this.priority).bind(Ticket::getPriority,Ticket::setPriority);
        binder.forField(this.status).bind(Ticket::getStatus, Ticket::setStatus);

        binder.forField(this.technicianCombobox).bind(Ticket::getTechnician, Ticket::setTechnician);

        addClassName("tickets-form");
        setClickListenerEvent();
        configureDialogLayout();
        add(dialogInteraction,dialogTechnician, mainLayout());

    }

    //Creat main layout
    public VerticalLayout mainLayout(){

        title.setWidth("100%");
        status.setWidth("100%");
        priority.setWidth("100%");

        userName.setWidth("100%");
        technicianName.setWidth("100%");

        visualizeInteraction.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        transferTo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        visualizeInteraction.setIcon(LineAwesomeIcon.ENVELOPE_OPEN.create());
        transferTo.setIcon(LineAwesomeIcon.HEADSET_SOLID.create());

        visualizeInteraction.setWidth(save.getWidth());
        transferTo.setWidth(save.getWidth());

        HorizontalLayout titleLayout = new HorizontalLayout(title);
        titleLayout.setWidthFull();


        HorizontalLayout statusPriority = new HorizontalLayout(status, priority);
        statusPriority.setWidthFull();

        // User fields layout
        HorizontalLayout userFields = new HorizontalLayout(userName, technicianName);
        userFields.setWidthFull();

        HorizontalLayout interactionsButtons = new HorizontalLayout(visualizeInteraction);
        interactionsButtons.setSpacing(true);
        interactionsButtons.setWidthFull();

        HorizontalLayout transferButton = new HorizontalLayout(transferTo);
        transferButton.setSpacing(true);
        transferButton.setWidthFull();


        HorizontalLayout defaultButtons = new HorizontalLayout(save, delete, close);
        defaultButtons.setSpacing(true);
        defaultButtons.setWidthFull();

        VerticalLayout layout = new VerticalLayout(
                titleLayout,
                statusPriority,
                userFields,
                interactionsButtons,
                transferButton,
                defaultButtons
        );

        layout.setWidth("100%"); // Makes sure the form layout fits its container
        layout.setSpacing(true); // Adds consistent spacing between vertical sections
        layout.setPadding(true); // Adds padding around the form layout for better aesthetics

        return layout;
    }

    public void setTicket(Ticket ticket){

        if (ticket != null){
            binder.setBean(ticket);
        }
    }

    private void validateAndSave(){

        if (title.getValue().trim().isEmpty()){
            Notification.show("Title cannot be empty ⚠");
            return;
        }

        if (userName.getValue().trim().isEmpty()){
            Notification.show("User was not set properly ⚠");
            return;
        }

        if (listInteractions.getItems().isEmpty()){
            Notification.show("Do at least one interaction ⚠");
            return;
        }

        if (binder.isValid()){
            fireEvent(new TicketFormEvent.SaveEvent(this,binder.getBean()));
        }
    }

    //Event fire when user needs to add a new Interaction.
    private void addNewInteraction(){

        if (interaction.getValue().trim().isEmpty()){
            dialogInteraction.close();
        }else{

            TicketInteractions ticketInteractions = new TicketInteractions();
            ticketInteractions.setDescription(interaction.getValue());
            ticketInteractions.setDateInteraction(LocalDateTime.now());

            tempInteractions.add(ticketInteractions);

            Ticket ticket = binder.getBean();

            //this must change
            ticketInteractions.setUser(ticket.getUser());
            ticketInteractions.setTicket(ticket);

            MessageListItem message = new MessageListItem();

            Instant timerToMessage = ticketInteractions.getDateInteraction().toInstant(ZoneOffset.UTC);
            message.setText(interaction.getValue());
            message.setUserColorIndex(1);
            message.setTime(timerToMessage);
            message.setUserName(ticket.getUser().getName());

            // Temporarily add the interaction to the list (without assigning a Ticket yet)
            tempInteractions.add(ticketInteractions);

            // Add the new message to the list of message items
            messageItems.add(message);

            // Update the MessageList with all messages
            listInteractions.setItems(messageItems);

            // Clear the interaction input field
            interaction.clear();

            dialogInteraction.close();

        }

    }

    //When user is able to set technician to a ticket, then we need this.
    private void setTechnicianToTiecket(){


        if (technicianCombobox.getValue() == null){
            dialogTechnician.close();
        }else{

            Users technician = technicianCombobox.getValue();

            Ticket currentTicket = binder.getBean();
            currentTicket.setTechnician(technician);

            technicianName.setValue(technician.getName());

            dialogTechnician.close();
        }

    }

    //Temp interactions when ticket isn't saved yet. This is able to store the ticket interaction before user save the ticket.
    public List<TicketInteractions> getTempInteractions(){
        return tempInteractions;
    }

    //Set dumpInteraction from the Interactions in  the current ticket.
    public void setDumpInteractions(List<TicketInteractions> listInteractions){

        this.dumpInteractions = listInteractions != null ? listInteractions : new ArrayList<>();

        // Clear the previous items
        messageItems.clear(); // Clear messageItems to avoid retaining previous messages

        // Create MessageListItems from dumpInteractions
        messageItems.addAll(dumpInteractions.stream()
                .map(interaction -> {
                    MessageListItem message = new MessageListItem();
                    message.setText(interaction.getDescription());
                    message.setUserColorIndex(1); // Set the color index as needed
                    message.setTime(interaction.getDateInteraction().toInstant(ZoneOffset.UTC));
                    message.setUserName(interaction.getUser().getName());
                    return message;
                })
                .toList()); // Collect to a list and add to messageItems

        // Update the listInteractions
        this.listInteractions.setItems(messageItems);
    }

    //Interaction text field and buttons, it will go in the footer of the dialogInteraction.
    public VerticalLayout createFooterDialogControl(){

        VerticalLayout interactionLayout = new VerticalLayout();

        interactionLayout.add(interaction, new HorizontalLayout(okInteracion, cancelInteraction));

        return interactionLayout;
    }

    //Events when user interact with buttons, save, delete, cancel, ok, cancel...
    private void setClickListenerEvent(){
        okInteracion.addClickListener(event -> addNewInteraction());
        cancelInteraction.addClickListener(event -> dialogInteraction.close());

        okTechnician.addClickListener(event -> setTechnicianToTiecket());
        cancelTechnician.addClickListener(event -> dialogTechnician.close());

        save.addClickListener(clickEvent -> validateAndSave());
        delete.addClickListener(clickEvent -> fireEvent(new TicketFormEvent.DeleteEvent(this,binder.getBean())));
        close.addClickListener(clickEvent -> fireEvent(new TicketFormEvent.CloseEvent(this)));

        binder.addStatusChangeListener(statusChangeEvent -> save.setEnabled(binder.isValid()));

    }
    public void configureDialogLayout(){
        interaction.setWidth("100%");
        interaction.setHeight("150px");
        interaction.getStyle().set("overflow", "auto");

        dialogInteraction.setHeaderTitle("Interactions");
        dialogInteraction.setWidth("75%");
        dialogInteraction.add(listInteractions);
        dialogInteraction.getFooter().add(new VerticalLayout(
                interaction
        ), new HorizontalLayout(okInteracion, cancelInteraction));

        VerticalLayout createLayoutDialogTechnician = new VerticalLayout();

        HorizontalLayout configureDialogButtonLayout = new HorizontalLayout();
        okTechnician.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelTechnician.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        configureDialogButtonLayout.add(new HorizontalLayout(okTechnician, cancelTechnician));

        createLayoutDialogTechnician.add(technicianCombobox);
        dialogTechnician.setHeaderTitle("Transfer a ticket");
        dialogTechnician.add(createLayoutDialogTechnician, configureDialogButtonLayout);

    }

    //Events
    public static abstract class TicketFormEvent extends ComponentEvent<TicketForm>{

        private Ticket ticket;

        public TicketFormEvent(TicketForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }

        public Ticket getTicket(){
            return ticket;
        }

        public static class SaveEvent extends  TicketFormEvent{
            SaveEvent(TicketForm source, Ticket ticket){
                super(source, ticket);
            }
        }

        public static class DeleteEvent extends TicketFormEvent{
            DeleteEvent(TicketForm source, Ticket ticket){
                super(source, ticket);
            }
        }

        public static class CloseEvent extends TicketFormEvent{
            CloseEvent(TicketForm source){
                super(source, null);
            }
        }
    }

    public Registration addDeleteListener(ComponentEventListener<TicketFormEvent.DeleteEvent> listener){
        return addListener(TicketFormEvent.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<TicketFormEvent.SaveEvent> listener){
        return addListener(TicketFormEvent.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<TicketFormEvent.CloseEvent> listener){
        return addListener(TicketFormEvent.CloseEvent.class, listener);
    }

}
