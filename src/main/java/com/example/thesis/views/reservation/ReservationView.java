package com.example.thesis.views.reservation;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.backend.reservation.Reservation;
import com.example.thesis.backend.reservation.ReservationRepository;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Route(value = ReservationView.ROUTE, layout = MainView.class)
@PageTitle("Reservations")
@CssImport("./styles/views/reservations/reservation-view.css")
@Secured(ReservationView.PRIVILEGE)
@Slf4j
public class ReservationView extends VerticalLayout {
    public static final String PRIVILEGE = "RESERVATION_VIEW_PRIVILEGE";
    public static final String ROUTE = "reservations";
    public static final String CHOOSE_PROPERTY = "Choose property:";
    public static final String CHOOSE_FLOOR = "Choose floor:";
    public static final String CHOOSE_DATE_AND_HOUR = "Choose date and hour:";
    public static final String CONFIRM = "Add new";
    private static final String HOUR_PATTERN = "HH:mm";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(HOUR_PATTERN);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PropertyRepository propertyRepository;

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final FloorRepository floorRepository;

    private ComboBox<Floor> chooseFloorBox;
    private ComboBox<Property> choosePropertyBox;
    private DateTimePicker dateTimePicker;
    private Label accordionLabel;
    private Accordion accordion;
    private List<AccordionPanel> reservationPanels;
    private Collection<Reservation> reservations;

    public ReservationView(UserRepository userRepository, PropertyRepository propertyRepository,
                           ReservationRepository reservationRepository, FloorRepository floorRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.reservationRepository = reservationRepository;
        this.floorRepository = floorRepository;

        setId("reservation-view");

        this.setAlignItems(Alignment.CENTER);
        String username = SecurityUtils.getLoggedUserUsername();
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        reservationPanels = new ArrayList<>();

        accordion = new Accordion();
        accordion.setId("accordion");

        choosePropertyBox = new ComboBox<>(CHOOSE_PROPERTY);
        choosePropertyBox.setId("choosePropertyBox");

        chooseFloorBox = new ComboBox<>(CHOOSE_FLOOR, user.getFloors());
        chooseFloorBox.setId("chooseFloorBox");

        chooseFloorBox.addValueChangeListener(e -> {
            refreshAccordionValues();
            Collection<Property> properties = this.propertyRepository.findByOwner(e.getValue());
            choosePropertyBox.setItems(properties);
        });

        choosePropertyBox.addValueChangeListener(e -> {
            refreshAccordionValues();
        });

        HorizontalLayout pickerAndConfirm = new HorizontalLayout();

        dateTimePicker = new DateTimePicker();
        dateTimePicker.setId("dateTimePicker");
        dateTimePicker.setLabel(CHOOSE_DATE_AND_HOUR);
        dateTimePicker.setLocale(Locale.GERMAN);
        dateTimePicker.setValue(findNextRoundTime());
        dateTimePicker.setStep(Duration.ofMinutes(30));
        //setMin(LocalDateTime min)
        //setMax(LocalDateTime max)
        dateTimePicker.addValueChangeListener(e -> {
            refreshAccordionValues();
        });

        Button confirmButton = new Button(CONFIRM);
        confirmButton.setId("confirm-button");
        confirmButton.addClickListener(e -> {
            Property property = choosePropertyBox.getValue();
            LocalDateTime start = dateTimePicker.getValue();
            Duration duration = Duration.ofHours(1); //TODO dodaj na widoku

            if (dateIsInPast(start)) {
                Notification.show("Choose a future date!");
                return;
            }

            if (reservationCollidesWithAnother(start, duration)) { //TODO check if start doesn't collide with existing reservation
                Notification.show("Chosen date collides with another reservation!");
                return;
            }

            Reservation reservation = Reservation.builder()
                    .user(user)
                    .property(property)
                    .duration(duration)
                    .start(start)
                    .build();

            reservationRepository.save(reservation);

            log.info("Reservation saved" + reservation.toString());
            Notification.show("Reservation added successfully");
            refreshAccordionValues();
        });

        pickerAndConfirm.add(dateTimePicker, confirmButton);

        accordionLabel = new Label("Existing reservations");
        accordionLabel.setVisible(false);
        accordion.setVisible(false);
        add(chooseFloorBox, choosePropertyBox, pickerAndConfirm, accordionLabel, accordion);
    }

    private LocalDateTime findNextRoundTime() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime lastHalf = time.truncatedTo(ChronoUnit.HOURS);
        return lastHalf.plusMinutes(30 * (time.getMinute() / 15) + 30);
    }

    private boolean reservationCollidesWithAnother(LocalDateTime start, Duration duration) {
        LocalDateTime end = start.plus(duration);

        return reservations.stream().anyMatch(reservation -> doesNotCollideWithNewReservation(start, end, reservation));
    }

    private boolean doesNotCollideWithNewReservation(LocalDateTime start, LocalDateTime end, Reservation reservation) {
        return reservationStartsDuring(start, end, reservation) || reservationStartsBefore(start, end, reservation)
                || reservationEndsDuring(start, end, reservation);
    }

    private boolean reservationStartsDuring(LocalDateTime start, LocalDateTime end, Reservation reservation) {
        LocalDateTime reservationStart = reservation.getStart();
        return start.isBefore(reservationStart) && end.isAfter(reservationStart);
    }

    private boolean reservationStartsBefore(LocalDateTime start, LocalDateTime end, Reservation reservation) {
        LocalDateTime reservationStart = reservation.getStart();
        LocalDateTime reservationEnd = reservationStart.plus(reservation.getDuration());
        return start.isAfter(reservationStart) && start.isBefore(reservationEnd)
                && end.isAfter(reservationStart) && end.isBefore(reservationEnd);
    }

    private boolean reservationEndsDuring(LocalDateTime start, LocalDateTime end, Reservation reservation) {
        LocalDateTime reservationEnd = reservation.getStart().plus(reservation.getDuration());
        return start.isBefore(reservationEnd) && end.isAfter(reservationEnd);
    }

    private boolean dateIsInPast(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    private void refreshAccordionValues() {
        if (chooseFloorBox.isEmpty() || choosePropertyBox.isEmpty()) {
            return;
        }
        accordionLabel.setVisible(true);
        accordion.setVisible(true);

        removeAllElementsFromAccordion();
        Floor choseFloor = chooseFloorBox.getValue();
        LocalDateTime now = LocalDateTime.now().minusMinutes(5);
        LocalDateTime dayEnd = LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MAX);
        reservations = reservationRepository.findByPropertyOwnerAndStartBetweenOrderByStartAsc(choseFloor, now, dayEnd); //TODO dodaj szukanie po property
        for (Reservation reservation : reservations) {
            AccordionPanel panel = createAccordionPanelForReservation(reservation);
            this.reservationPanels.add(panel);
            accordion.add(panel).addThemeVariants(DetailsVariant.FILLED);
        }
    }

    private AccordionPanel createAccordionPanelForReservation(Reservation reservation) {
        String start = dateTimeFormatter.format(reservation.getStart());
        String end = dateTimeFormatter.format(reservation.getStart().plus(reservation.getDuration()));
        String time = start + " - " + end;
        Span content = new Span(reservation.toString());
        return new AccordionPanel(time, content);
    }

    private void removeAllElementsFromAccordion() {
        for (AccordionPanel panel : reservationPanels) {
            accordion.remove(panel);
        }
        reservationPanels.clear();
    }
}