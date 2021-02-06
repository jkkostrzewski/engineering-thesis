package com.example.thesis.views.auth;

import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.backend.security.auth.ForgotPasswordToken;
import com.example.thesis.backend.security.auth.TokenService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = ForgotPasswordView.ROUTE)
@PageTitle("Forgot password")
@CssImport("./styles/views/auth/forgot-password-view.css")
public class ForgotPasswordView extends VerticalLayout {

    public static final String ROUTE = "/forgot-password";

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final TokenService tokenService;

    public ForgotPasswordView(EmailService emailService, TokenService tokenService) {
        this.emailService = emailService;
        this.tokenService = tokenService;

        TextField email = new TextField("Enter your email");
        Button confirm = new Button("Confirm");
        confirm.addClickListener(event -> {
            ForgotPasswordToken token = new ForgotPasswordToken(email.getValue());
            tokenService.saveForgotPasswordToken(token);
            emailService.sendForgotPasswordTokenEmail(email.getValue(), token);
            Notification.show("Email sent successfully!");
        });

        add(email, confirm);

        setAlignItems(Alignment.CENTER);
    }
}
