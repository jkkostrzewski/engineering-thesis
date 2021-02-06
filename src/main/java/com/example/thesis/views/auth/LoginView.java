package com.example.thesis.views.auth;

import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.views.notice.board.NoticeBoardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = LoginView.ROUTE)
@PageTitle(LoginView.TITLE)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "login";
    public static final String ACTION = "login";
    public static final String TITLE = "Dorm app";

    private LoginForm login = new LoginForm();

    @Autowired
    private UserRepository userRepository;

    public LoginView(UserRepository userRepository) {
        this.userRepository = userRepository;
        setId("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction(ACTION);
        login.setForgotPasswordButtonVisible(true);
        login.addLoginListener(event -> UI.getCurrent().navigate(NoticeBoardView.class, "Main Board"));
        login.addForgotPasswordListener(event -> UI.getCurrent().navigate(ForgotPasswordView.class));

        add(new H1(TITLE));
        add(login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getLocation()
                .getQueryParameters()
                .getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
