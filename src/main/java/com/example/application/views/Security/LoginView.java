package com.example.application.views.Security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;

@Route(value = "login")
@PageTitle("Desk Mantra | Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm loginForm;

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm = new LoginForm();
        loginForm.setAction("login");
        //Add CSRF token to form
        UI.getCurrent().getPage().executeJs(
                "document.querySelector('vaadin-login-form').addEventListener('login', function(e) {" +
                        "  const csrfToken = document.cookie.split('; ')" +
                        "    .find(row => row.startsWith('XSRF-TOKEN'))" +
                        "    .split('=')[1];" +
                        "  e.detail.form.appendChild(" +
                        "    Object.assign(document.createElement('input'), {" +
                        "      type: 'hidden'," +
                        "      name: '_csrf'," +
                        "      value: csrfToken" +
                        "    })" +
                        "  );" +
                        "});"
        );

        add(loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
