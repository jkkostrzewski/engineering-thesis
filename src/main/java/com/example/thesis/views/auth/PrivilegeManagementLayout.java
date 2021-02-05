package com.example.thesis.views.auth;

import com.example.thesis.backend.security.auth.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.access.annotation.Secured;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Secured(PrivilegeManagementLayout.PRIVILEGE)
public class PrivilegeManagementLayout extends FormLayout {

    public static final String PRIVILEGE = "PRIVILEGE_MANAGEMENT_PRIVILEGE";

    private final UserService userService;

    private CheckboxGroup<Privilege> privileges = new CheckboxGroup<>();
    private User user;

    public PrivilegeManagementLayout(UserService userService) {
        this.userService = userService;

        HorizontalLayout vertical = new HorizontalLayout();
        vertical.setId("privilege-management-layout");
        TextField username = new TextField("Enter username");
        username.setId("privilege-management-username");
        Button check = new Button("Check");
        check.setId("privilege-management-check");

        Button confirm = new Button("Confirm");
        confirm.addClickListener(e -> changeUserPrivileges());
        confirm.setVisible(false);

        check.addClickListener(e -> {
            if (userService.existsByUsername(username.getValue())) {
                if (Objects.nonNull(privileges)) {
                    privileges.removeAll();
                }

                user = userService.findByUsername(username.getValue()).get();                 //TODO zamień get
                privileges.setItems(userService.findAll());
                privileges.select(user.getRole().getPrivileges());
                confirm.setVisible(true);
            }
        });


        vertical.add(username, check);
        add(vertical);

        add(privileges, confirm);
    }

    private void changeUserPrivileges() {
        if (userIsAdmin()) {
            Notification.show("You can't change admin privileges!");
            return;
        }

        List<Privilege> selected = new ArrayList<>(privileges.getSelectedItems());
//        user.getRoles().forEach(roleRepository::delete); //TODO delete previous role if not used
        Role newRole = updateRole(user.getEmail(), selected);
        userService.saveRole(newRole);
        user.setRole(newRole);
        userService.save(user);

        Notification.show("User privileges changed successfully");
    }

    private boolean userIsAdmin() {
        return user.getUsername().equalsIgnoreCase("admin");
    }

    @Transactional
    public Role updateRole(String name, List<Privilege> privileges) {

        Role role = userService.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        userService.save(role);
        return role;
    }
}
