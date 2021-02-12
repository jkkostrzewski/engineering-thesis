package com.example.thesis.views.reservation;

import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.backend.reservation.ReservationService;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.HashMap;
import java.util.Map;

@Route(value = ReservationView.ROUTE, layout = MainView.class)
@PageTitle("Reservations")
@CssImport("./styles/views/reservations/reservation-view.css")
@Secured(ReservationView.PRIVILEGE)
@Slf4j
@Tag("reservation-view")
public class ReservationView extends VerticalLayout {
    public static final String PRIVILEGE = "RESERVATION_VIEW_PRIVILEGE";
    public static final String ROUTE = "reservations";

    @Autowired
    private final UserService userService;

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private final PropertyService propertyService;

    public ReservationView(UserService userService,
                           ReservationService reservationService,
                           PropertyService propertyService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.propertyService = propertyService;

        Tab reserveTab = new Tab("Reservation");
        ReserveTab reserveTabContent = new ReserveTab(userService, reservationService, propertyService);

        Tab myReservationsTab = new Tab("My Reservations");
        MyReservationsTab myReservationsTabContent = new MyReservationsTab(userService, reservationService, propertyService);
        myReservationsTabContent.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(reserveTab, reserveTabContent);
        tabsToPages.put(myReservationsTab, myReservationsTabContent);
        Tabs tabs = new Tabs(reserveTab, myReservationsTab);
        Div tabsContent = new Div(reserveTabContent, myReservationsTabContent);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, tabsContent);
        setAlignItems(Alignment.CENTER);
    }
}