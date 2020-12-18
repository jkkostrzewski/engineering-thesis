package com.example.thesis.views.auth;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.security.SecurityConfiguration;
import com.example.thesis.backend.security.auth.Token;
import com.example.thesis.backend.security.auth.TokenRepository;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.views.main.MainView;
import com.example.thesis.views.notice.board.NoticeBoardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Arrays;

@Slf4j
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
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private final FloorRepository floorRepository;

    public UserRegistrationView(UserRepository userRepository, EmailService emailService, TokenRepository tokenRepository, FloorRepository floorRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.floorRepository = floorRepository;
        TextField emailField = new TextField("Email");

        ComboBox<String> chooseDefaultRole = new ComboBox<>("Choose default role");
        chooseDefaultRole.setItems(Arrays.asList(USER, FLOOR_ADMIN, ADMIN));

        ComboBox<Floor> mainFloorSelector = new ComboBox<>("Choose main floor");    //TODO add multiple choice
        mainFloorSelector.setItems(this.floorRepository.findAll());

        Button confirm = new Button("Confirm");

        confirm.addClickListener(e -> {
            String email = emailField.getValue();

            if(!this.userRepository.existsByEmail(email)) {
                String role = chooseDefaultRole.getOptionalValue().orElseThrow(RuntimeException::new);
                Token token = new Token(role, emailField.getValue(), mainFloorSelector.getValue());      //TODO poszukaj istniejącego tokena - jesli istnieje i jest włączony to go przypisz
                //TODO it can send email without creating token

                this.emailService.sendRegistrationEmail(email, token);
                this.tokenRepository.save(token);
                Notification.show("Invitation email sent successfully");

                UI.getCurrent().navigate(SecurityConfiguration.LOGIN_SUCCESS_URL.substring(1));
            } else {
                Notification.show("User with given email already exists!");
            }
        });

        add(emailField, chooseDefaultRole, mainFloorSelector, confirm);
        this.setAlignItems(Alignment.CENTER);
    }
}
