package com.example.thesis.views.auth;

import com.example.thesis.backend.security.auth.Token;
import com.example.thesis.backend.security.auth.TokenRepository;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Arrays;

@Route(value = UserRegistrationView.ROUTE, layout = MainView.class)
@PageTitle("Add a new user")
@CssImport("./styles/views/auth/user-registration-view.css")
@Secured(UserRegistrationView.PRIVILEGE)
public class UserRegistrationView extends VerticalLayout {
    public static final String PRIVILEGE = "USER_REGISTRATION_VIEW_PRIVILEGE";
    public static final String ROUTE = "/register-user";
    public static final String USER = "User";
    public static final String FLOOR_ADMIN = "FloorAdmin";
    public static final String ADMIN = "Admin";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenRepository tokenRepository;

    public UserRegistrationView() {
        TextField emailField = new TextField("Email");

        ComboBox<String> chooseDefaultRole = new ComboBox<>("Choose default role");
        chooseDefaultRole.setItems(Arrays.asList(USER, FLOOR_ADMIN, ADMIN));

        Button confirm = new Button("Confirm");

        confirm.addClickListener(e -> {
            String email = emailField.getValue();

            if(!userRepository.existsByEmail(email)) {
                String role = chooseDefaultRole.getOptionalValue().orElseThrow(RuntimeException::new);
                Token token = new Token(role);      //TODO poszukaj istniejącego tokena - jesli istnieje i jest włączony to go przypisz
                //TODO it can send email without creating token

                emailService.sendRegistrationEmail(email, token);
                tokenRepository.save(token);
                Notification.show("Invitation email sent successfully");
            } else {
                Notification.show("User with given email already exists!");
            }
        });

        add(emailField, chooseDefaultRole, confirm);
        this.setAlignItems(Alignment.CENTER);
    }
}
