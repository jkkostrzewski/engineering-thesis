package com.example.thesis.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "login", layout = MainView.class)
@PageTitle("Log in")
@CssImport("./styles/views/login/login.css")
@RouteAlias(value = "", layout = MainView.class)
public class LoginView extends VerticalLayout {

    private TextField username;
    private PasswordField password;
    private Button confirm;

    public LoginView() {
        setId("login-view");
        username = new TextField("Username");
        password = new PasswordField("Password");
        confirm = new Button("Confirm");
        add(username, password, confirm);
        confirm.addClickListener( e-> {
            Notification.show("Logging in with credentials: " + username.getValue() + " " + password.getValue());
        });
    }

}
