package com.example.application.views.Role;

import com.example.application.configurationComponents.ButtonConfiguration;
import com.example.application.data.Permissions;
import com.example.application.data.Role;
import com.example.application.data.Users;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Set;

public class RoleForm extends FormLayout {


    TextField name = new TextField("Role name");
    TextField description = new TextField("Role description");
    ButtonConfiguration buttonConfiguration = new ButtonConfiguration();

    Button save = buttonConfiguration.getSaveButton();
    Button delete = buttonConfiguration.getDeleteButton();
    Button close = buttonConfiguration.getCloseButton();

    BeanValidationBinder<Role> binder = new BeanValidationBinder<>(Role.class);


    MultiSelectComboBox<Permissions> permissions = new MultiSelectComboBox<>("Permissions");


    public RoleForm(Set<Permissions> permissions, Users user) {

        addClassName("role-form");

        setClickListenerEvent();
        add(mainLayout());
        binder.bind(this.name, "name");
        binder.bind(this.description, "description");

       binder.forField(this.permissions)
                .bind(Role::getPermissions, Role::setPermissions);

       this.permissions.setItems(permissions);
       this.permissions.setItemLabelGenerator(Permissions::getPermission);

    }



    private VerticalLayout mainLayout(){

        VerticalLayout layout = new VerticalLayout();

        name.setWidth("100%");
        description.setWidth("100%");

        layout.add(name, description,
                new HorizontalLayout(permissions),
                new HorizontalLayout(save, delete, close));

        layout.setPadding(true);
        layout.setSpacing(true);

        return layout;

    }

    private void setClickListenerEvent(){

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this,binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this,null)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    }

    public void setRole(Role role){
        binder.setBean(role);
    }

    private void validateAndSave(){
        if(binder.isValid()){
            fireEvent( new SaveEvent(this,binder.getBean()));
        }
    }


    //events
    public static abstract class RoleFormEvent extends ComponentEvent<RoleForm>{
        private Role role;

        protected RoleFormEvent(RoleForm source, Role role){
            super(source, false);
            this.role = role;
        }

        public Role getRole(){
            return role;
        }

    }

    public static class SaveEvent extends RoleFormEvent{
        SaveEvent(RoleForm source, Role role){
            super(source, role);
        }
    }
    public static class DeleteEvent extends RoleFormEvent{
        DeleteEvent(RoleForm source, Role role){
            super(source, role);
        }
    }
    public static class CloseEvent extends RoleFormEvent{
        CloseEvent(RoleForm source, Role role){
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener){
        return addListener(DeleteEvent.class, listener);
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener){
        return addListener(CloseEvent.class, listener);
    }

}
