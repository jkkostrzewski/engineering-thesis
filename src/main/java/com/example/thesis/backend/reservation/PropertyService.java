package com.example.thesis.backend.reservation;

import com.example.thesis.backend.floor.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PropertyService {

    @Autowired
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Collection<Property> findByOwner(Floor owner) {
        return propertyRepository.findByOwner(owner);
    }
}
