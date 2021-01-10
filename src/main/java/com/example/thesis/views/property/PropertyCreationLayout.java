package com.example.thesis.views.property;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PropertyCreationLayout extends VerticalLayout {

    private final PropertyManagementView propertyManagementView;
    private final PropertyService propertyService;

    private final TextField name;
    private final ComboBox<Floor> availableFloors;
    private final Button confirm;

    public PropertyCreationLayout(PropertyManagementView propertyManagementView, PropertyService propertyService) {
        this.propertyManagementView = propertyManagementView;
        this.propertyService = propertyService;

        name = new TextField("Enter property name");
        availableFloors = new ComboBox<>("Choose a floor");
        availableFloors.setItems(propertyService.findAllFloors());

        confirm = new Button("Confirm");
        confirm.addClickListener(e -> addProperty());

        add(name, availableFloors, confirm);
    }

    private void addProperty() {
        if (name.isEmpty() || availableFloors.isEmpty()) {
            return;
        }

        Property property = Property.builder().name(name.getValue()).owner(availableFloors.getValue()).build();
        propertyService.save(property);
        propertyManagementView.refreshGrid();

        Notification.show("Property created successfully");
    }
}
