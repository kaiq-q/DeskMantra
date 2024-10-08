package com.example.application.views.Users;

import com.example.application.configurationComponents.ButtonConfiguration;
import com.example.application.data.Role;
import com.example.application.data.Users;
import com.example.application.tools.SetToListConverter;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import java.util.*;


public class UsersForm extends FormLayout {


    BeanValidationBinder<Users> binder = new BeanValidationBinder<>(Users.class);
    ButtonConfiguration buttonConfiguration = new ButtonConfiguration();

    TextField name = new TextField("Full name");
    EmailField email = new EmailField("Email");
    TextField login = new TextField("Username");

    MultiSelectComboBox<Role> roles  = new MultiSelectComboBox("Roles");

    Button save = buttonConfiguration.getSaveButton();
    Button delete = buttonConfiguration.getDeleteButton();
    Button close = buttonConfiguration.getCloseButton();

    public UsersForm(List<Role> roleList){
        addClassName("users-form");

        binder.bind(name, "name");
        binder.bind(email, "email");
        binder.bind(login, "login");

        // Bind the roles field and specify the converter using the SetToListConverter
        binder.forField(this.roles)
                .withConverter(new SetToListConverter())
                .bind("roles");

        // Set the available items and label generator for the roles MultiSelectComboBox
        this.roles.setItems(roleList);
        this.roles.setItemLabelGenerator(Role::getName);

        name.setPattern("[\\p{L}\\p{M}\\s]+");
        name.setTooltipText("Eg: Sophia Schmitt ");
        name.setRequiredIndicatorVisible(true);

        roles.setTooltipText("Eg: Admin");

        this.login.setRequiredIndicatorVisible(true);
        this.login.setAllowedCharPattern("[\\p{L}\\p{N}\\.\\-]+");

        setClickListenerEvent();

        add(mainLayout());
    }

    public VerticalLayout mainLayout(){

        VerticalLayout layout = new VerticalLayout();

        name.setWidth("100%");
        email.setWidth("100%");
        login.setWidth("100%");
        roles.setWidth("100%");

        layout.add(name, email, login, roles, new HorizontalLayout(save, delete, close));

        layout.setWidth("100%");
        layout.setSpacing(true);
        layout.setPadding(true);

        return layout;

    }

    public void setUsers(Users user){
        if (user != null) {
            // Check if roles property is null or empty
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                // Initialize roles property with an empty set or any default roles as needed
                user.setRoles(new ArrayList<>()); // Assuming roles is a Set
            }

            // Clear existing selection in the ComboBox
            this.roles.deselectAll();

            for (Role role : user.getRoles()) {
                if (user.getRoles().stream().anyMatch(r -> r.getId().equals(role.getId()))) {
                    this.roles.select(role);
                }
            }

            binder.setBean(user);


        }
    }

    private void setClickListenerEvent() {

        save.addClickListener(buttonClickEvent -> validateAndSave());
        delete.addClickListener(buttonClickEvent -> fireEvent(new UsersFormEvent.DeleteEvent(this,binder.getBean())));
        close.addClickListener(buttonClickEvent -> fireEvent(new UsersFormEvent.CloseEvent(this)));

        binder.addStatusChangeListener(statusChangeEvent -> save.setEnabled(binder.isValid()));

    }

    private void validateAndSave() {
        if (binder.isValid()){
            fireEvent(new UsersFormEvent.SaveEvent(this,binder.getBean()));
        }
    }

    //Events
    public static abstract class UsersFormEvent extends ComponentEvent<UsersForm>{


        private Users user;

        public UsersFormEvent(UsersForm source, Users user) {
            super(source, false);
            this.user = user;
        }

        public Users getUsers(){
            return user;
        }

        public static class SaveEvent extends UsersFormEvent{
            SaveEvent(UsersForm source, Users user){
                super(source, user);
            }
        }

        public static class DeleteEvent extends UsersFormEvent{
            DeleteEvent(UsersForm source, Users user){
                super(source, user);
            }
        }

        public static class CloseEvent extends UsersFormEvent{
            CloseEvent(UsersForm source){
                super(source,null);
            }
        }

    }

    public Registration addDeleteListener(ComponentEventListener<UsersFormEvent.DeleteEvent> listener){
        return addListener(UsersFormEvent.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<UsersFormEvent.SaveEvent> listener){
        return addListener(UsersFormEvent.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<UsersFormEvent.CloseEvent> listener) {
        return addListener(UsersFormEvent.CloseEvent.class, listener);
    }

}
