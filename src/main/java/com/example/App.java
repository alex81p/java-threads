package com.example;

import com.example.model.ParkingLot;
import com.example.services.ParkingLotManager;
import com.example.services.TrafficGenerator;

import java.util.concurrent.TimeUnit;

public class App {
    public static void main( String[] args ) {
        ParkingLot parkingLot = new ParkingLot(3);
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLot, true);
        TrafficGenerator trafficGenerator = new TrafficGenerator(parkingLotManager, 1,3,
                5,10,2,3, TimeUnit.SECONDS);
        trafficGenerator.generate(10);
    }
}
