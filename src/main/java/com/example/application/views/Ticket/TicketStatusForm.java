package com.example.application.views.Ticket;

import com.example.application.configurationComponents.ButtonConfiguration;
import com.example.application.data.TicketStatus;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Breaks;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TicketStatusForm extends FormLayout {

    BeanValidationBinder<TicketStatus> binder = new BeanValidationBinder<>(TicketStatus.class);
    ButtonConfiguration buttonConfiguration = new ButtonConfiguration();

    TextField status = new TextField("Status name");
    TextArea description = new TextArea("Description");
    Checkbox isDefault = new Checkbox("Default status when opening a ticket");

    Button save = buttonConfiguration.getSaveButton();

    Button delete = buttonConfiguration.getDeleteButton();
    Button close = buttonConfiguration.getCloseButton();

    public TicketStatusForm(List<TicketStatus> allTicketStatus) {
        addClassName("ticket-status-form");

        binder.bind(status, "status");
        binder.bind(description, "description");
        binder.forField(isDefault)
                        .bind(TicketStatus::isDefault, TicketStatus::setDefault);

        setClickListenerEvent();

        add(mainLayout());

    }

    private VerticalLayout mainLayout(){

        status.setWidth("100%");
        description.setWidth("100%");

        status.setRequiredIndicatorVisible(true);


        description.setMaxHeight("200px");
        description.setMinHeight("100px");
        description.setRequiredIndicatorVisible(true);

        VerticalLayout layout = new VerticalLayout(status, description, isDefault,
                                new HorizontalLayout(save, delete, close));

        layout.setPadding(true);
        layout.setSpacing(true);

        return layout;

    }

    private void setClickListenerEvent() {

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new StatusFormEvent.DeleteEvent(this,binder.getBean())));
        close.addClickListener(event -> fireEvent(new StatusFormEvent.CloseEvent(this)));

    }

    private void validateAndSave() {

        if ((status.getValue().trim().isEmpty())){
           Notification.show("Status name field cannot be empty ⚠", 3000, Notification.Position.MIDDLE);
           return;
        }

        if (description.getValue().trim().isEmpty()){
            Notification.show("Description field cannot be empty ⚠", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (binder.isValid()){
            fireEvent(new StatusFormEvent.SaveEvent(this, binder.getBean()));
        }

    }

    public void setStatus(TicketStatus ticketStatus){
        if (ticketStatus != null){
            binder.setBean(ticketStatus);
        }
    }

    //Events
    public static abstract class StatusFormEvent extends ComponentEvent<TicketStatusForm>{

        private TicketStatus status;

        public StatusFormEvent(TicketStatusForm source, TicketStatus status) {
            super(source, false);
            this.status = status;
        }

        public TicketStatus getStatus(){return status;}

        public static class SaveEvent extends StatusFormEvent{
            SaveEvent(TicketStatusForm source, TicketStatus status){super(source, status);}
        }

        public static class DeleteEvent extends StatusFormEvent {
            DeleteEvent(TicketStatusForm source, TicketStatus status){
                super(source, status);
            }
        }

        public static class CloseEvent extends  StatusFormEvent{
            CloseEvent(TicketStatusForm source) {super(source, null);}
        }

    }

    public Registration addDeleteListener(ComponentEventListener<StatusFormEvent.DeleteEvent> listener) {
        return addListener(StatusFormEvent.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<StatusFormEvent.SaveEvent> listener){
        return addListener(StatusFormEvent.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<StatusFormEvent.CloseEvent> listener){
        return addListener(StatusFormEvent.CloseEvent.class, listener);
    }

}
