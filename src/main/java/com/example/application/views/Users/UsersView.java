package com.example.application.views.Users;
import com.example.application.data.Role;
import com.example.application.data.Users;
import com.example.application.services.RoleService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Collectors;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends VerticalLayout {

    TextField uniqueFilter = new TextField();
    Grid<Users> grid = new Grid<>(Users.class);
    UsersForm form;
    UserService userService;
    RoleService roleService;


    public UsersView(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
        addClassName("users-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setUsers(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void saveUser(UsersForm.UsersFormEvent.SaveEvent saveEvent) {

        userService.saveUser(saveEvent.getUsers());
        updateList();
        closeEditor();

    }
    private void deleteUser(UsersForm.UsersFormEvent.DeleteEvent deleteEvent) {
        userService.deleteUser(deleteEvent.getUsers());
        updateList();
        closeEditor();
    }
    private void updateList() {

        grid.setItems(userService.findAllUsers(uniqueFilter.getValue()));

    }
    private void configureForm() {


        form = new UsersForm(roleService.findAllRoles(""));
        form.setWidth("40em");

        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(e -> closeEditor());

    }
    private void configureGrid() {
        grid.addClassNames("users-grid");
        grid.setSizeFull();
        grid.setColumns("name", "email", "login");
        grid.addColumn(users -> users.getRoles().stream().map(Role::getName).collect(Collectors.toList())).setHeader("Roles");
        grid.getColumns().forEach(usersColumn -> usersColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editUsers(event.getValue()));
    }
    public void editUsers(Users user) {

        if (user == null){
            closeEditor();
        }else{
            form.setUsers(user);
            form.setVisible(true);
            addClassName("editing");
        }

    }
    private HorizontalLayout getToolbar(){

        uniqueFilter.setPlaceholder("Type you search...");
        uniqueFilter.setClearButtonVisible(true);
        uniqueFilter.setValueChangeMode(ValueChangeMode.LAZY);
        uniqueFilter.addValueChangeListener(e -> updateList());

        Button addUser = new Button("Add new user");
        addUser.addClickListener(click -> addNewUser());

        HorizontalLayout toolbar = new HorizontalLayout(uniqueFilter, addUser);
        toolbar.addClassName("toolbar");
        return toolbar;

    }
    private void addNewUser() {
        grid.asSingleSelect().clear();
        editUsers(new Users());
    }
    private HorizontalLayout getContent(){

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1,form);
        content.addClassName("content");
        content.setSizeFull();
        return content;

    }
}

