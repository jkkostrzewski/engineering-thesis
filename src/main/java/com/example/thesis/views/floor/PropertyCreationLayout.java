package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class PropertyCreationLayout extends VerticalLayout {

    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;

    private final TextField name;
    private final ComboBox<Floor> availableFloors;
    private final Button confirm;

    public PropertyCreationLayout(PropertyRepository propertyRepository, FloorRepository floorRepository) {
        this.propertyRepository = propertyRepository;
        this.floorRepository = floorRepository;

        name = new TextField("Enter property name");
        availableFloors = new ComboBox<>("Choose a floor");
        availableFloors.setItems(floorRepository.findAll());

        confirm = new Button("Confirm");
        confirm.addClickListener(e -> addProperty());

        add(name, availableFloors, confirm);
    }

    private void addProperty() {
        Property property = Property.builder().name(name.getValue()).owner(availableFloors.getValue()).build();
        propertyRepository.save(property);

        Notification.show("Property created successfully");
    }
}
