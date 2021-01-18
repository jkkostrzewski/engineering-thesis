package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.FloorService;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.backend.security.auth.UserService;
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

    private final FloorService floorService;
    private final NoticeService noticeService;
    private final UserService userService;
    private final PropertyService propertyService;

    @Autowired
    public FloorManagementView(FloorService floorService, NoticeService noticeService, UserService userService,
                               PropertyService propertyService) {
        this.floorService = floorService;
        this.noticeService = noticeService;
        this.userService = userService;
        this.propertyService = propertyService;

        Accordion accordion = new Accordion();
        accordion.setWidthFull();

        FloorCreationLayout floorCreationLayout = new FloorCreationLayout(floorService, noticeService, userService);
        accordion.add("Floor creation", floorCreationLayout);

        FloorGridLayout floorGridLayout = new FloorGridLayout(floorService);
        accordion.add("Floor management", floorGridLayout);

        add(accordion);

        this.setWidthFull();
    }


}
