package com.example.thesis.backend.reservation;

import com.example.thesis.backend.floor.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Collection<Reservation> findByPropertyOwnerAndPropertyAndStartBetweenOrderByStartAsc(Floor owner, Property property, LocalDateTime start, LocalDateTime end);
}
