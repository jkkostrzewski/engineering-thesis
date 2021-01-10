package com.example.thesis.views.property;

import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;

@Slf4j
@Route(value = PropertyManagementView.ROUTE, layout = MainView.class)
@PageTitle("Property management")
@CssImport("./styles/views/floor/floor-management-view.css")
@Secured(PropertyManagementView.PRIVILEGE)
public class PropertyManagementView extends VerticalLayout {

    public static final String ROUTE = "/property-management";
    public static final String PRIVILEGE = "PROPERTY_MANAGEMENT_VIEW_PRIVILEGE";

    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;
    private final PropertyCreationLayout propertyCreationLayout;
    private final PropertyGridLayout propertyGridLayout;


    public PropertyManagementView(PropertyRepository propertyRepository, FloorRepository floorRepository) {
        this.propertyRepository = propertyRepository;
        this.floorRepository = floorRepository;

        Accordion accordion = new Accordion();
        accordion.setWidthFull();

        propertyCreationLayout = new PropertyCreationLayout(this, propertyRepository, floorRepository);
        accordion.add("Property creation", propertyCreationLayout);

        propertyGridLayout = new PropertyGridLayout(propertyRepository, floorRepository);
        accordion.add("Property management", propertyGridLayout);

        add(accordion);
        this.setWidthFull();
    }

    public void refreshGrid() {
        this.propertyGridLayout.refreshGrid();
    }

}
