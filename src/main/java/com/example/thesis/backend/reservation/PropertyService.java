package com.example.thesis.backend.reservation;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, FloorRepository floorRepository) {
        this.propertyRepository = propertyRepository;
        this.floorRepository = floorRepository;
    }

    public Collection<Property> findByOwner(Floor owner) {
        return propertyRepository.findByOwner(owner);
    }

    public void save(Property property) {
        propertyRepository.save(property);
    }

    public Collection<Floor> findAllFloors() {
        return floorRepository.findAll();
    }

    public Collection<Property> findAll() {
        return propertyRepository.findAll();
    }

    public void delete(Property property) {
        propertyRepository.delete(property);
    }
}
