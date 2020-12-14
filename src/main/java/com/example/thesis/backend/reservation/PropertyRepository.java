package com.example.thesis.backend.reservation;

import com.example.thesis.backend.floor.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Collection<Property> findByOwner(Floor owner);
}
