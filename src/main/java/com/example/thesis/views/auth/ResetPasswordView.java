package com.example.thesis.views.auth;

import com.example.thesis.backend.security.auth.TokenService;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.profile.ProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route(value = ResetPasswordView.ROUTE)
@PageTitle("Reset password")
@CssImport("./styles/views/auth/reset-password-view.css")
public class ResetPasswordView extends VerticalLayout implements HasUrlParameter<String> {

    public static final String ROUTE = "/reset-password";

    @Autowired
    private final UserService userService;

    @Autowired
    private final TokenService tokenService;

    private final PasswordField password;
    private final PasswordField confirmPassword;

    private User user;

    public ResetPasswordView(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;

        password = new PasswordField("Enter your new password");
        confirmPassword = new PasswordField("Confirm your new password");

        Button confirm = new Button("Confirm");
        confirm.addClickListener(event -> {
            if(passwordFieldsMatch()) {
                userService.changePassword(user, password.getValue());

                UI.getCurrent().navigate(LoginView.class);
                Notification.show("Password has been changed.");
            } else {
                Notification.show("Passwords do not match!");
            }
        });

        add(password, confirmPassword, confirm);

        setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        String email;
        if (parameter.contains("@")) {
            email = parameter;
        } else {
            email = tokenService.findByTokenId(UUID.fromString(parameter)).getEmail();
        }
        user = userService.findByEmail(email);
    }

    private boolean passwordFieldsMatch() {
        return password.getValue().equals(confirmPassword.getValue());
    }
}