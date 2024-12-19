package com.example.application.views.home;


import com.example.application.security.Emails;
import com.example.application.services.RoleService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;

import javax.mail.MessagingException;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class HomeView extends Composite<VerticalLayout> {

    RoleService roleService;
    Button button = new Button("Test email", event -> sendEmail());


    VerticalLayout layout = new VerticalLayout();
    private FlexLayout dashboardLayout;
    private int chartCount = 0;



    public HomeView() {

        //test

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        // Set up MultiSelectComboBox


        // Initialize the FlexLayout for the dashboard
        dashboardLayout = new FlexLayout();
        dashboardLayout.setSizeFull();
        dashboardLayout.getStyle().set("flex-wrap", "wrap");
        dashboardLayout.getStyle().set("gap", "10px");

        // Add initial components to the dashboard
        addDashboardComponent("Chart 1");
        addDashboardComponent("Chart 2");

        // Button to add new components dynamically
        Button addButton = new Button("Add Chart", event -> addDashboardComponent("New Chart " + (++chartCount)));

        // Add components to the HomeView layout
        layout.add(button, addButton, dashboardLayout);
        getContent().add(layout); // Add everything to the main layout
    }

    private void addDashboardComponent(String title) {
        // Create a card layout for the dashboard component
        VerticalLayout card = new VerticalLayout();
        card.setWidth("300px");
        card.setHeight("200px");
        card.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "5px")
                .set("padding", "10px")
                .set("background-color", "#f9f9f9");

        // Add title to the card

        card.add(title);


        // Add a remove button to each card
        Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
        removeButton.getElement().setAttribute("theme", "icon small");
        removeButton.addClickListener(e -> dashboardLayout.remove(card));
        card.add(removeButton);

        H1 h1 = new H1("Technician");
        card.add(h1);


        // Add the card to the dashboard layout
        dashboardLayout.add(card);
    }

   private void sendEmail(){

        Emails emails = new Emails();
       try {
           emails.sendEmail("kaique.silva@astetech.com.br", "Test Subject", "This is a test email body.");
           System.out.println("Email sent successfully.");
       } catch (MessagingException e) {
           e.printStackTrace();
           System.out.println("Failed to send email: " + e.getMessage());
       }

   }
}

