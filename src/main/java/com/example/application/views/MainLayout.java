package com.example.application.views;
import com.example.application.views.Role.RoleView;
import com.example.application.views.Ticket.TicketPriorityView;
import com.example.application.views.Ticket.TicketStatusView;
import com.example.application.views.Ticket.TicketView;
import com.example.application.views.Users.UsersView;
import com.example.application.views.home.HomeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        toggle.setTooltipText("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE,  LumoUtility.Flex.GROW);



        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Desk Mantra");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Home", HomeView.class, LineAwesomeIcon.HOME_SOLID.create()));

        SideNavItem userLink = new SideNavItem("Users",
                UsersView.class, LineAwesomeIcon.USER_FRIENDS_SOLID.create());
        userLink.addItem(new SideNavItem("Roles",  RoleView.class, LineAwesomeIcon.SKETCH.create()));

        SideNavItem ticketLink = new SideNavItem("Tickets",
                TicketView.class, LineAwesomeIcon.TICKET_ALT_SOLID.create());
        ticketLink.addItem(new SideNavItem("Status", TicketStatusView.class, LineAwesomeIcon.YAMMER.create()));
        ticketLink.addItem(new SideNavItem("Priority", TicketPriorityView.class, LineAwesomeIcon.CIRCLE_SOLID.create()));

        nav.addItem(userLink, ticketLink);
        //nav.addItem(new SideNavItem("Users", UsersView.class, LineAwesomeIcon.USER_FRIENDS_SOLID.create()));
        //nav.addItem(new SideNavItem("Roles", RoleView.class, LineAwesomeIcon.SKETCH.create()));
        //nav.addItem(new SideNavItem("Tickets", TicketView.class, LineAwesomeIcon.TICKET_ALT_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
