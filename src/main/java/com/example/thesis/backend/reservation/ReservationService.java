package com.example.thesis.backend.reservation;

import com.example.thesis.backend.floor.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Collection<Reservation> findByPropertyOwnerAndPropertyAndStartBetweenOrderByStartAsc(Floor chosenFloor, Property value, LocalDateTime startDayTime, LocalDateTime dayTimeEnd) {
        return reservationRepository.findByPropertyOwnerAndPropertyAndStartBetweenOrderByStartAsc(chosenFloor, value, startDayTime, dayTimeEnd);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
