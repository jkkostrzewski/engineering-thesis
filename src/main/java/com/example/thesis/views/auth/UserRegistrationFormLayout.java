package com.example.thesis.views.auth;

import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.security.SecurityConfiguration;
import com.example.thesis.backend.security.auth.Token;
import com.example.thesis.backend.security.auth.TokenRepository;
import com.example.thesis.backend.security.auth.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Arrays;

public class UserRegistrationFormLayout extends FormLayout {

    public static final String USER = "User";
    public static final String FLOOR_ADMIN = "FloorAdmin";
    public static final String ADMIN = "Admin";

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final FloorRepository floorRepository;

    public UserRegistrationFormLayout(UserRepository userRepository, EmailService emailService, TokenRepository tokenRepository, FloorRepository floorRepository) {
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
    }
}
