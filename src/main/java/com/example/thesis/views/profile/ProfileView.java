package com.example.thesis.views.profile;

import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.auth.ResetPasswordView;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = ProfileView.ROUTE, layout = MainView.class)
@PageTitle("Profile view")
@CssImport("./styles/views/profile/profile-view.css")
@Secured(ProfileView.PRIVILEGE)
public class ProfileView extends VerticalLayout implements HasUrlParameter<String> {

    public static final String ROUTE = "/profile";
    public static final String PRIVILEGE = "PROFILE_VIEW_PRIVILEGE";

    private final UserService userService;

    private final TextField firstName;
    private final TextField lastName;
    private final TextField username;
    private final TextField email;

    private User user;

    @Autowired
    public ProfileView(UserService userService) {
        this.userService = userService;

        firstName = new TextField("First Name");
        firstName.setEnabled(false);

        lastName = new TextField("Last Name");
        lastName.setEnabled(false);

        username = new TextField("Username");
        username.setEnabled(false);

        HorizontalLayout emailChange = new HorizontalLayout();
        email = new TextField("Email");
        Button changeEmail = new Button("Change");
        changeEmail.addClickListener(event -> {
            userService.changeUserEmail(user.getEmail(), email.getValue()); //TODO sprawdz czy sie zmienia email
            Notification.show("Email has been changed");
        });
        emailChange.add(email, changeEmail);

        Button changePassword = new Button("Change password");
        changePassword.addClickListener(event -> {
            UI.getCurrent().navigate(ResetPasswordView.class, user.getEmail());
        });

        add(firstName, lastName, username, emailChange, changePassword);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String profileUsername) {
        user = userService.findByUsername(profileUsername).get();

        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        username.setValue(user.getUsername());
        email.setValue(user.getEmail());
    }
}
