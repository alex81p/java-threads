package com.example.model;

import com.example.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private List<ParkingSpot> parkingSpots;

    public ParkingLot(int capacity) {
        if (capacity < 1) {
            throw new ParkingLotException("Parking lot capacity should be more then zero");
        }
        parkingSpots = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            parkingSpots.add(new ParkingSpot(i + 1));
        }
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }
}
