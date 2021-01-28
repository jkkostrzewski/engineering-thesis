package com.example.thesis.views.reservation;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.reservation.*;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.main.MainView;
import com.example.thesis.views.utilities.DateFormatters;
import com.vaadin.flow.component.Tag;
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
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Route(value = ReservationView.ROUTE, layout = MainView.class)
@PageTitle("Reservations")
@CssImport("./styles/views/reservations/reservation-view.css")
@Secured(ReservationView.PRIVILEGE)
@Slf4j
@Tag("reservation-view")
public class ReservationView extends VerticalLayout {
    public static final String PRIVILEGE = "RESERVATION_VIEW_PRIVILEGE";
    public static final String ROUTE = "reservations";
    public static final String CHOOSE_PROPERTY = "Choose property:";
    public static final String CHOOSE_FLOOR = "Choose floor:";
    public static final String CHOOSE_DATE_AND_HOUR = "Choose date and hour:";
    public static final String CONFIRM = "Add new";

    @Autowired
    private final UserService userService;

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private final PropertyService propertyService;

    @Id("chooseFloorBox")
    private ComboBox<Floor> chooseFloorBox;

    @Id("choosePropertyBox")
    private ComboBox<Property> choosePropertyBox;

    @Id("dateTimePicker")
    private DateTimePicker dateTimePicker;

    private Label accordionLabel;

    @Id("accordion")
    private Accordion accordion;
    private List<AccordionPanel> reservationPanels;
    private Collection<Reservation> reservations;

    @Id("Duration")
    private final ComboBox<Duration> durationSelector;

    private final User user;

    public ReservationView(UserService userService,
                           ReservationService reservationService,
                           PropertyService propertyService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.propertyService = propertyService;

        this.setAlignItems(Alignment.CENTER);
        String username = SecurityUtils.getLoggedUserUsername();
        user = userService.findByUsername(username).orElseThrow(RuntimeException::new);

        reservationPanels = new ArrayList<>();

        accordion = new Accordion();

        choosePropertyBox = new ComboBox<>(CHOOSE_PROPERTY);
        chooseFloorBox = new ComboBox<>(CHOOSE_FLOOR, user.getFloors());        //TODO findAllFloors raczej z warningiem że rezerwuje z innego piętra niż jego własny

        chooseFloorBox.addValueChangeListener(e -> {
            refreshAccordionValues();
            Collection<Property> properties = propertyService.findByOwner(e.getValue());
            choosePropertyBox.setItems(properties);
        });

        choosePropertyBox.addValueChangeListener(e -> {
            refreshAccordionValues();
        });

        HorizontalLayout pickerAndConfirm = new HorizontalLayout();

        dateTimePicker = new DateTimePicker();          //TODO create datePicker and TimePicker separately that way i can remove night hours from time
        dateTimePicker.setLabel(CHOOSE_DATE_AND_HOUR);
        dateTimePicker.setLocale(Locale.GERMAN);         //TODO try to disable manual editing of hour
        dateTimePicker.setValue(findNextRoundTime());
        dateTimePicker.setStep(Duration.ofMinutes(30));
        dateTimePicker.addValueChangeListener(e -> refreshAccordionValues());

        durationSelector = new ComboBox<>("Duration");
        durationSelector.setItems(Duration.ofHours(1), Duration.ofHours(2), Duration.ofHours(3), Duration.ofHours(4));

        Button confirmButton = new Button(CONFIRM);
        confirmButton.setId("confirm-button");
        confirmButton.addClickListener(e -> {
            Property property = choosePropertyBox.getValue();
            LocalDateTime start = dateTimePicker.getValue();
            Duration duration = durationSelector.getValue();

            if (Objects.isNull(duration)) {
                Notification.show("Choose a duration!");
                return;
            }

            if (dateIsInPast(start)) {
                Notification.show("Choose a future date!");
                return;
            }

            if (reservationCollidesWithAnother(start, duration)) { //TODO check if start doesn't collide with existing reservation
                Notification.show("Chosen date collides with another reservation!");
                return;
            }

            if (!dateTimeInAvailableHours()) {
                Notification.show("You cannot reserve a property during night time!");
                return;
            }

            if (timeIsNotDividedByHalfHour()) {
                Notification.show("You can reserve by using whole hours or halves!");
            }

            Reservation reservation = Reservation.builder()
                    .creationTime(Instant.now())
                    .user(user)
                    .property(property)
                    .duration(duration)
                    .start(start)
                    .build();

            reservationService.save(reservation);

            log.info("Reservation saved" + reservation.toString());
            Notification.show("Reservation added successfully");
            refreshAccordionValues();
        });

        pickerAndConfirm.add(dateTimePicker, confirmButton);

        accordionLabel = new Label("Existing reservations");
        accordionLabel.setVisible(false);
        accordion.setVisible(false);
        add(chooseFloorBox, choosePropertyBox, pickerAndConfirm, durationSelector, accordionLabel, accordion);
    }

    private boolean timeIsNotDividedByHalfHour() {
        int minute = dateTimePicker.getValue().getMinute();
        return minute != 0 && minute != 30;
    }

    private boolean dateTimeInAvailableHours() {
        LocalDateTime startDateTime = dateTimePicker.getValue();
        LocalDateTime endDateTime = startDateTime.plus(durationSelector.getValue());
        return startDateTime.isAfter(LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(7, 59, 59)))
                && startDateTime.isBefore(LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(22, 0, 1)))
                && endDateTime.isBefore(LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(22, 0, 1)));
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
                || reservationEndsDuring(start, end, reservation) || reservationStartsAtTheSameTime(start, reservation)
                || reservationEndsAtTheSameTime(end, reservation);
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

    private boolean reservationStartsAtTheSameTime(LocalDateTime start, Reservation reservation) {
        return start.isEqual(reservation.getStart());
    }

    private boolean reservationEndsAtTheSameTime(LocalDateTime end, Reservation reservation) {
        LocalDateTime reservationEnd = reservation.getStart().plus(reservation.getDuration());
        return end.isEqual(reservationEnd);
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
//        LocalDateTime startDayTime = findStartDateTime();   //TODO DOESNT SHOW ANYTHING
        LocalDateTime startDayTime = LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MIN);
        LocalDateTime dayTimeEnd = LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MAX);
        reservations = reservationService.findByPropertyOwnerAndPropertyAndStartBetweenOrderByStartAsc(choseFloor, choosePropertyBox.getValue(), startDayTime, dayTimeEnd); //TODO dodaj szukanie po property
        for (Reservation reservation : reservations) {
            AccordionPanel panel = createAccordionPanelForReservation(reservation);
            this.reservationPanels.add(panel);
            accordion.add(panel).addThemeVariants(DetailsVariant.FILLED);
        }
    }

//    private LocalDateTime findStartDateTime() {
//        LocalDate date = dateTimePicker.getValue().toLocalDate();
//        if (date.isEqual(LocalDate.now())) {
//            return LocalDateTime.of(date, LocalTime.now());
//        }
//        return LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MIN);
//    }

    private AccordionPanel createAccordionPanelForReservation(Reservation reservation) {
        String start = DateFormatters.HOUR_MINUTE.format(reservation.getStart());
        String end = DateFormatters.HOUR_MINUTE.format(reservation.getStart().plus(reservation.getDuration()));
        String time = start + " - " + end;

        String contentHTML = "Created by: " + reservation.getUser().getFullName() + "<br>"
                + "Creation time: " + DateFormatters.STANDARD_DATE_TIME.format(reservation.getCreationTime()) + "<br>";
        Span content = new Span();
        content.getElement().setProperty("innerHTML", contentHTML);

        AccordionPanel panel = new AccordionPanel(time, content);

        if (user.equals(reservation.getUser())) {
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