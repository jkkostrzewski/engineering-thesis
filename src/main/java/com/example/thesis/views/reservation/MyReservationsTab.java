package com.example.thesis.views.reservation;

import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.backend.reservation.ReservationService;
import com.example.thesis.backend.security.auth.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MyReservationsTab extends VerticalLayout {

    private final UserService userService;
    private final ReservationService reservationService;
    private final PropertyService propertyService;

    public MyReservationsTab(UserService userService, ReservationService reservationService,
                             PropertyService propertyService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.propertyService = propertyService;

        Button showHistoricReservations = new Button("Show historic reservations");

        add(showHistoricReservations);
        setAlignItems(Alignment.CENTER);
    }
}
