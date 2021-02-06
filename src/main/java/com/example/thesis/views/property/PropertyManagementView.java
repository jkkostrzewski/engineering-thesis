package com.example.thesis.views.property;

import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Slf4j
@Route(value = PropertyManagementView.ROUTE, layout = MainView.class)
@PageTitle("Property management")
@CssImport("./styles/views/floor/floor-management-view.css")
@Secured(PropertyManagementView.PRIVILEGE)
public class PropertyManagementView extends VerticalLayout {

    public static final String ROUTE = "/property-management";
    public static final String PRIVILEGE = "PROPERTY_MANAGEMENT_VIEW_PRIVILEGE";

    @Autowired
    private final PropertyService propertyService;

    private final PropertyCreationLayout propertyCreationLayout;
    private final PropertyGridLayout propertyGridLayout;

    @Id("accordion")
    private final Accordion accordion;

    public PropertyManagementView(PropertyService propertyService) {
        this.propertyService = propertyService;

        accordion = new Accordion();
        accordion.setWidthFull();

        propertyCreationLayout = new PropertyCreationLayout(this, propertyService);
        accordion.add("Property creation", propertyCreationLayout);

        propertyGridLayout = new PropertyGridLayout(propertyService);
        accordion.add("Property management", propertyGridLayout);

        add(accordion);
        this.setWidthFull();
    }

    public void refreshGrid() {
        this.propertyGridLayout.refreshGrid();
    }

}
