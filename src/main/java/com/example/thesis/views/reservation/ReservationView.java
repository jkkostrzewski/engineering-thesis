package com.example.thesis.views.reservation;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.backend.reservation.Reservation;
import com.example.thesis.backend.reservation.ReservationRepository;
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
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

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
    private List<AccordionPanel> reservations;

    public ReservationView(UserRepository userRepository, PropertyRepository propertyRepository,
                           ReservationRepository reservationRepository, FloorRepository floorRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.reservationRepository = reservationRepository;
        this.floorRepository = floorRepository;

        setId("reservation-view");

        this.setAlignItems(Alignment.CENTER);

        reservations = new ArrayList<>();

        List<Floor> floors = this.floorRepository.findAll();
        accordion = new Accordion();
        accordion.setId("accordion");

        choosePropertyBox = new ComboBox<>(CHOOSE_PROPERTY);
        choosePropertyBox.setId("choosePropertyBox");

        chooseFloorBox = new ComboBox<>(CHOOSE_FLOOR, floors);
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
        LocalDateTime now = LocalDateTime.now();
        dateTimePicker.setValue(now);
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
            String username = (String) VaadinSession.getCurrent().getAttribute("name");
            User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
            LocalDateTime start = dateTimePicker.getValue();
            if (start.isBefore(LocalDateTime.now())) {      //TODO check if start doesn't collide with existing reservation
                Notification.show("Choose a future date!");
                return;
            }
            Duration duration = Duration.ofHours(1); //TODO dodaj na widoku

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

    private void refreshAccordionValues() {
        if (chooseFloorBox.isEmpty() || choosePropertyBox.isEmpty()) {
            return;
        }
        accordionLabel.setVisible(true);
        accordion.setVisible(true);

        removeAllElementsFromAccordion();
        Floor choseFloor = chooseFloorBox.getValue();
        Collection<Reservation> reservations;
        LocalDateTime dayStart = LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MIN);
        LocalDateTime dayEnd = LocalDateTime.of(dateTimePicker.getValue().toLocalDate(), LocalTime.MAX);
        reservations = reservationRepository.findByPropertyOwnerAndStartBetweenOrderByStartAsc(choseFloor, dayStart, dayEnd); //TODO dodaj szukanie po property
        for (Reservation reservation : reservations) {
            String start = dateTimeFormatter.format(reservation.getStart());
            String end = dateTimeFormatter.format(reservation.getStart().plus(reservation.getDuration()));
            String time = start + " - " + end;
            Span content = new Span(reservation.toString());
            AccordionPanel panel = new AccordionPanel(time, content);
            this.reservations.add(panel);
            accordion.add(panel).addThemeVariants(DetailsVariant.FILLED);
        }
    }

    private void removeAllElementsFromAccordion() {
        for (AccordionPanel panel : reservations) {
            accordion.remove(panel);
        }
        reservations.clear();
    }
}