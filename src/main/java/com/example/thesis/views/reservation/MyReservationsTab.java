package com.example.thesis.views.reservation;

import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.backend.reservation.ReservationService;
import com.example.thesis.backend.security.auth.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MyReservationsTab extends VerticalLayout {

    public static final String SHOW_HISTORIC_RESERVATIONS = "Show historic reservations";
    public static final String HIDE_HISTORIC_RESERVATIONS = "Hide historic reservations";

    private final UserService userService;
    private final ReservationService reservationService;
    private final PropertyService propertyService;

    public MyReservationsTab(UserService userService, ReservationService reservationService,
                             PropertyService propertyService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.propertyService = propertyService;

        HorizontalLayout historic = new HorizontalLayout();
        Button showHistoric = new Button(SHOW_HISTORIC_RESERVATIONS);
        showHistoric.addClickListener(event -> {
            showHistoric.setText(showHistoric.getText().equals(SHOW_HISTORIC_RESERVATIONS) ?
                    HIDE_HISTORIC_RESERVATIONS : SHOW_HISTORIC_RESERVATIONS);
        });
        historic.add(showHistoric);

        add(historic);
        setAlignItems(Alignment.CENTER);
    }
}
