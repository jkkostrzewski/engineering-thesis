package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = FloorManagementView.ROUTE, layout = MainView.class)
@PageTitle("Floor management")
@CssImport("./styles/views/floor/floor-management-view.css")
@Secured(FloorManagementView.PRIVILEGE)
public class FloorManagementView extends VerticalLayout {

    public static final String ROUTE = "/add-floor";
    public static final String PRIVILEGE = "ADD_FLOOR_VIEW_PRIVILEGE";

    @Autowired
    private final FloorRepository floorRepository;

    @Autowired
    private final NoticeBoardRepository noticeBoardRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PropertyRepository propertyRepository;

    public FloorManagementView(FloorRepository floorRepository, NoticeBoardRepository noticeBoardRepository,
                               UserRepository userRepository, PropertyRepository propertyRepository) {
        this.floorRepository = floorRepository;
        this.noticeBoardRepository = noticeBoardRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;

        Accordion accordion = new Accordion();
        accordion.setWidthFull();

        FloorCreationLayout floorCreationLayout = new FloorCreationLayout(floorRepository, noticeBoardRepository, userRepository);
        accordion.add("Floor creation", floorCreationLayout);

//        PropertyManagementView propertyCreationLayout = new PropertyManagementView(propertyRepository, floorRepository);
//        accordion.add("Property management", propertyCreationLayout);

        add(accordion);

//        this.setAlignItems(Alignment.CENTER);
    }


}
