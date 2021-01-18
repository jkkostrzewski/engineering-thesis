package com.example.thesis.views.auth;

import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorService;
import com.example.thesis.backend.security.auth.Token;
import com.example.thesis.backend.security.auth.TokenService;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.notice.board.NoticeBoardView;
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

    private final UserService userService;
    private final EmailService emailService;
    private final FloorService floorService;

    public UserRegistrationFormLayout(UserService userService, EmailService emailService, FloorService floorService) {
        this.userService = userService;
        this.emailService = emailService;
        this.floorService = floorService;

        TextField emailField = new TextField("Email");

        ComboBox<String> chooseDefaultRole = new ComboBox<>("Choose default role");
        chooseDefaultRole.setItems(Arrays.asList(USER, FLOOR_ADMIN, ADMIN));

        ComboBox<Floor> mainFloorSelector = new ComboBox<>("Choose main floor");    //TODO add multiple choice
        mainFloorSelector.setItems(floorService.findAll());

        Button confirm = new Button("Confirm");

        confirm.addClickListener(e -> {
            String email = emailField.getValue();

            if(!userService.existsByEmail(email)) {
                String role = chooseDefaultRole.getOptionalValue().orElseThrow(RuntimeException::new);
                Token token = new Token(role, emailField.getValue(), mainFloorSelector.getValue());      //TODO poszukaj istniejącego tokena - jesli istnieje i jest włączony to go przypisz
                //TODO it can send email without creating token

                this.emailService.sendRegistrationEmail(email, token);
                userService.saveToken(token);
                Notification.show("Invitation email sent successfully");

                UI.getCurrent().navigate(NoticeBoardView.class, "Main Board");
            } else {
                Notification.show("User with given email already exists!");
            }
        });

        add(emailField, chooseDefaultRole, mainFloorSelector, confirm);
    }
}
