package com.example.thesis.views.reservation;

import com.example.thesis.backend.reservation.PropertyService;
import com.example.thesis.backend.reservation.Reservation;
import com.example.thesis.backend.reservation.ReservationService;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.utilities.DateFormatters;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyReservationsTab extends VerticalLayout {

    private final UserService userService;
    private final ReservationService reservationService;
    private final PropertyService propertyService;

    private Accordion accordion;
    private List<AccordionPanel> reservationPanels;
    private Collection<Reservation> reservations;
    private User currentUser;

    public MyReservationsTab(UserService userService, ReservationService reservationService,
                             PropertyService propertyService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.propertyService = propertyService;

        currentUser = userService.findByUsername(SecurityUtils.getLoggedUserUsername()).get();

        reservationPanels = new ArrayList<>();
        accordion = new Accordion();
        accordion.setId("my-reservations-accordion");
        add(accordion);
        refreshAccordionValues();

        setAlignItems(Alignment.CENTER);
    }

    private void refreshAccordionValues() {
        removeAllElementsFromAccordion();

        reservations = reservationService.findByUserOrderByStartAsc(currentUser);
        for (Reservation reservation : reservations) {
            AccordionPanel panel = createAccordionPanelForReservation(reservation);
            this.reservationPanels.add(panel);
            accordion.add(panel).addThemeVariants(DetailsVariant.FILLED);
        }
    }

    private AccordionPanel createAccordionPanelForReservation(Reservation reservation) {
        String date = DateFormatters.STANDARD_DATE.format(reservation.getStart());
        String start = DateFormatters.HOUR_MINUTE.format(reservation.getStart());
        String end = DateFormatters.HOUR_MINUTE.format(reservation.getStart().plus(reservation.getDuration()));
        String time = date + " " + start + " - " + end;

        String contentHTML = "Created by: " + reservation.getUser().getFullName() + "<br>"
                + "Creation time: " + DateFormatters.STANDARD_DATE_TIME.format(reservation.getCreationTime()) + "<br>";
        Span content = new Span();
        content.getElement().setProperty("innerHTML", contentHTML);

        AccordionPanel panel = new AccordionPanel(time, content);

        if (currentUser.equals(reservation.getUser())) {
            Button delete = new Button("Delete");
            delete.addClickListener(e -> {
                reservationService.delete(reservation);
                refreshAccordionValues();
                Notification.show("Reservation has been deleted");
            });
            panel.addContent(delete);
        }

        return panel;
    }

    private void removeAllElementsFromAccordion() {
        for (AccordionPanel panel : reservationPanels) {
            accordion.remove(panel);
        }
        reservationPanels.clear();
    }
}
