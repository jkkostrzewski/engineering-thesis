package com.example.thesis.views.auth;

import com.example.thesis.backend.floor.FloorService;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.*;
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

    private final UserService userService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final FloorService floorService;

    @Autowired
    public UserManagementView(UserService userService, EmailService emailService, TokenService tokenService, FloorService floorService) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.floorService = floorService;
        log.info("User " + SecurityUtils.getLoggedUserUsername() + " has entered the view", UserManagementView.class);

        Accordion accordion = new Accordion();
        UserRegistrationFormLayout userRegistration = new UserRegistrationFormLayout(userService, this.emailService, floorService);
        accordion.add("User registration", userRegistration);

//        if (SecurityUtils.userHasRole(PrivilegeManagementLayout.PRIVILEGE)) {       //TODO DODAC DLA WSZYSTKICH
            PrivilegeManagementLayout privilegeManagement = new PrivilegeManagementLayout(userService);
            accordion.add("Privilege management", privilegeManagement);
//        }

        add(accordion);
    }
}
