package com.example.application.views.Ticket;

import com.example.application.configurationComponents.ButtonConfiguration;
import com.example.application.data.TicketPriority;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TicketPriorityForm extends FormLayout {

    BeanValidationBinder<TicketPriority> binder = new BeanValidationBinder<>(TicketPriority.class);
    ButtonConfiguration buttonConfiguration = new ButtonConfiguration();

    TextField priority = new TextField("Priority name");
    TextArea  description = new TextArea("Description");
    Checkbox  isDefault = new Checkbox("Default priority when opening a ticket.");

    Button save   = buttonConfiguration.getSaveButton();
    Button delete = buttonConfiguration.getDeleteButton();
    Button close  = buttonConfiguration.getCloseButton();

    public TicketPriorityForm(List<TicketPriority> allTicketPriotity){

        addClassName("ticket-priority-form");
        binder.bind(priority, "priority");
        binder.bind(description, "description");
        binder.forField(isDefault)
                .bind(TicketPriority::isDefault, TicketPriority::setDefault);


        setClickListenerEvent();
        add(mainLayout());


    }

    private VerticalLayout mainLayout(){

        priority.setRequired(true);
        priority.setWidth("100%");

        description.setWidth("100%");
        description.setMaxHeight("200px");
        description.setMinHeight("100px");
        description.setRequired(true);


        VerticalLayout layout = new VerticalLayout(priority, description, isDefault,
                                new HorizontalLayout(save, delete, close));

        layout.setSpacing(true);
        layout.setPadding(true);

        return layout;
    }

    private void setClickListenerEvent() {

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new TicketPriorityForm.PriotiryFormEvent.DeleteEvent(this,binder.getBean())));
        close.addClickListener(event -> fireEvent(new TicketPriorityForm.PriotiryFormEvent.CloseEvent(this)));


    }


    private void validateAndSave(){

        if ((priority.getValue().trim().isEmpty())){

            Notification.show("Priority name field cannot be empty ⚠", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (description.getValue().trim().isEmpty()){
            Notification.show("Description field cannot be empty ⚠", 3000, Notification.Position.MIDDLE);
            return;
        }


        if (binder.isValid()){
            fireEvent(new PriotiryFormEvent.SaveEvent(this,binder.getBean()));
        }
    }


    public void setPriority(TicketPriority ticketPriority){
        if (ticketPriority != null){
            binder.setBean(ticketPriority);
        }
    }


    //Events
    public static abstract class PriotiryFormEvent extends ComponentEvent<TicketPriorityForm> {

       private TicketPriority priority;

        public PriotiryFormEvent(TicketPriorityForm source, TicketPriority priority) {
            super(source, false);
            this.priority = priority;
        }

        public TicketPriority getPriority() {
            return priority;
        }
        
        public static class SaveEvent extends PriotiryFormEvent{
            SaveEvent(TicketPriorityForm source, TicketPriority priority){
                super(source, priority);
            }
        }

        public static class DeleteEvent extends PriotiryFormEvent{
            DeleteEvent(TicketPriorityForm source, TicketPriority priority){
                super(source, priority);
            }
        }

        public static class CloseEvent extends PriotiryFormEvent{
            CloseEvent(TicketPriorityForm source){
                super(source, null);
            }
        }

    }

    public Registration addSaveListener(ComponentEventListener<PriotiryFormEvent.SaveEvent> eventListener){
        return addListener(PriotiryFormEvent.SaveEvent.class, eventListener);
    }

    public Registration addDeleteListener(ComponentEventListener<PriotiryFormEvent.DeleteEvent> eventListener){
        return addListener(PriotiryFormEvent.DeleteEvent.class, eventListener);
    }

    public Registration addCloseListener(ComponentEventListener<PriotiryFormEvent.CloseEvent> eventListener){
        return addListener(PriotiryFormEvent.CloseEvent.class, eventListener);
    }

}
