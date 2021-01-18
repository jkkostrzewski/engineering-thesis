package com.example.thesis.backend.floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FloorService {

    private final FloorRepository floorRepository;

    @Autowired
    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    public Collection<Floor> findAll() {
        return floorRepository.findAll();
    }

    public void delete(Floor floor) {
        floorRepository.delete(floor);
    }

    public void save(Floor floor) {
        floorRepository.save(floor);
    }
}
