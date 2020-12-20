package com.example.thesis.views.auth;

import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.PrivilegeRepository;
import com.example.thesis.backend.security.auth.RoleRepository;
import com.example.thesis.backend.security.auth.TokenRepository;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.backend.emails.EmailService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Slf4j
@Route(value = UserManagementView.ROUTE, layout = MainView.class)
@PageTitle("User management")
@CssImport("./styles/views/auth/user-management-view.css")
@Secured(UserManagementView.PRIVILEGE)
public class UserManagementView extends VerticalLayout {
    public static final String PRIVILEGE = "USER_REGISTRATION_VIEW_PRIVILEGE";
    public static final String ROUTE = "/register-user";

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private final FloorRepository floorRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PrivilegeRepository privilegeRepository;

    public UserManagementView(UserRepository userRepository, EmailService emailService, TokenRepository tokenRepository, FloorRepository floorRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        log.info("User " + userRepository.findByUsername(SecurityUtils.getLoggedUserUsername()).orElseThrow(RuntimeException::new).toString() + " has entered the view", UserManagementView.class);

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.floorRepository = floorRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;

        Accordion accordion = new Accordion();
        UserRegistrationFormLayout userRegistration = new UserRegistrationFormLayout(userRepository, emailService, tokenRepository, floorRepository);
        accordion.add("User registration", userRegistration);

//        if (SecurityUtils.userHasRole(PrivilegeManagementLayout.PRIVILEGE)) {       //TODO DODAC DLA WSZYSTKICH
            PrivilegeManagementLayout privilegeManagement = new PrivilegeManagementLayout(userRepository, roleRepository, privilegeRepository);
            accordion.add("Privilege management", privilegeManagement);
//        }

        add(accordion);
    }
}
