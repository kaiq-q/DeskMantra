package com.example.application.views.Role;


import com.example.application.data.Role;
import com.example.application.data.Users;
import com.example.application.services.PermissionsService;
import com.example.application.services.RoleService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Roles")
@Route(value = "users/roles", layout = MainLayout.class)
public class RoleView extends VerticalLayout {


    TextField roleNameField = new TextField();
    Grid<Role> grid = new Grid<>(Role.class);
    RoleForm form;
    RoleService roleService;
    PermissionsService permissionsService;
    UserService userService;

    Users user = new Users();

    public RoleView(RoleService roleService, PermissionsService permissionsService, UserService userService) {
        this.roleService = roleService;
        this.permissionsService = permissionsService;
        this.userService = userService;
        addClassName("role-view");
        setSizeFull();
        configureGrid();
        configureForms();

        add(getToolbar(),getContent());
        updateList();
        closeEditor();

    }



    private void configureGrid(){
        grid.addClassNames("role-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description");

        grid.getColumns().forEach(roleColumn -> roleColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editRole(event.getValue()));

   }

    private HorizontalLayout getToolbar(){

        roleNameField.setPlaceholder("Filter role by name...");
        roleNameField.setClearButtonVisible(true);
        roleNameField.setValueChangeMode(ValueChangeMode.LAZY);
        roleNameField.addValueChangeListener(name -> updateList());

        Button addRoleButton = new Button("Add role");
        addRoleButton.addClickListener(click -> addRole());

        HorizontalLayout toolbar = new HorizontalLayout(roleNameField, addRoleButton);
        toolbar.addClassName("toolbar");
        return toolbar;

   }

    private void configureForms() {

        user = userService.findById(10);
        form = new RoleForm(permissionsService.findAllPermissions(), user);
        form.setWidth("25em");

        form.addCloseListener(event -> closeEditor());
        form.addDeleteListener(this::deleteRole);
        form.addSaveListener(this::saveRole);

    }

    private Component getContent(){

        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(2,grid);
        content.setFlexGrow(1,form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void updateList(){
        grid.setItems(roleService.findAllRoles(roleNameField.getValue()));
    }

    private void closeEditor(){
        form.setRole(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveRole(RoleForm.SaveEvent event){
        roleService.saveRole(event.getRole());
        updateList();
        closeEditor();
    }
    private void deleteRole(RoleForm.DeleteEvent event){
        roleService.deleteRole(event.getRole());
        updateList();
        closeEditor();
    }

    public void editRole(Role role){
        if (role == null){
            closeEditor();
        } else {
            form.setRole(role);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addRole(){
        grid.asSingleSelect().clear();
        editRole(new Role());
    }





}
