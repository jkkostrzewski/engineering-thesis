package com.example.thesis.views.auth;

import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.security.auth.*;
import com.example.thesis.backend.security.utilities.DefaultPrivilegeProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Route(value = TokenRegistrationView.ROUTE)
@PageTitle("Register an account")
@CssImport("./styles/views/auth/token-registration-view.css")
public class TokenRegistrationView extends VerticalLayout implements HasUrlParameter<String> {
    public static final String ROUTE = "/token-registration";
    public static final String MAIN_FLOOR_NAME = "Main";

    @Autowired
    private final TokenRepository tokenRepository;
    
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository userRepository;
    
    @Autowired
    private final DefaultPrivilegeProvider defaultPrivilegeProvider;

    @Autowired
    private final FloorRepository floorRepository;

    private TextField firstName;
    private Label info;
    private TextField lastName;
    private TextField username;
    private EmailField email;
    private PasswordField password;
    private Button register;
    private PasswordField confirmPassword;

    private Token token;
    private TextField floor;

    public TokenRegistrationView(TokenRepository tokenRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, DefaultPrivilegeProvider defaultPrivilegeProvider, FloorRepository floorRepository) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.defaultPrivilegeProvider = defaultPrivilegeProvider;
        this.floorRepository = floorRepository;

        setId("token-registration");

        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        token = tokenRepository.findByUuid(UUID.fromString(parameter));

        if (token.isDisabled()) {
            UI.getCurrent().navigate("/token-register-error-page"); //TODO create default error page
        }

        info = new Label("Registration form");
        firstName = new TextField("First name");
        lastName = new TextField("Last name");
        username = new TextField("Username");
        email = new EmailField("Email");
        email.setValue(token.getEmail());
        email.setEnabled(false);
        floor = new TextField("Floor");
        floor.setValue(token.getMainFloor().getName());
        floor.setEnabled(false);
        password = new PasswordField("Password");
        confirmPassword = new PasswordField("Confirm password");

        register = new Button("Register");
        register.addClickListener(e -> {
            registerNewUser();
        });

        add(info, firstName, lastName, email, floor, username, password, confirmPassword, register);
    }

    private void registerNewUser() {
        if (passwordFieldsMatch()) {
            User user = new User(
                    firstName.getValue(),
                    lastName.getValue(),
                    username.getValue(),
                    email.getValue(),
                    passwordEncoder.encode(password.getValue()),
                    true,
                    true,
                    true,
                    true
            );

            user.addFloor(floorRepository.findByName(MAIN_FLOOR_NAME)); //TODO przeniesc tą stałą do klasy konfiguracyjnej
            user.addFloor(token.getMainFloor());

            setRoleFromToken(user);
            userRepository.save(user);

            Notification.show("User registered successfully");
            UI.getCurrent().navigate(LoginView.class);
        } else {
            Notification.show("Passwords don't match!");
        }
    }

    private void setRoleFromToken(User user) { //TODO refactor
        String roleString = token.getRole();
        Role role;
        switch (roleString) {
            case "User":
                role = defaultPrivilegeProvider.user(username.getValue());
                break;
            case "FloorAdmin":
                role = defaultPrivilegeProvider.floorAdmin(username.getValue());
                break;
            case "Admin":
                role = defaultPrivilegeProvider.admin(username.getValue());
                break;
            default:
                throw new RuntimeException();
        }
        user.setRole(role);
    }

    private boolean passwordFieldsMatch() {
        return password.getValue().equals(confirmPassword.getValue());
    }
}
