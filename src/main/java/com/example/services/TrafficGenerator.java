package com.example.services;

import com.example.model.Car;

import java.util.concurrent.TimeUnit;

public class TrafficGenerator {

    private final ParkingLotManager parkingLotManager;
    private final long intervalFrom;
    private final long intervalTo;
    private final long parkingDurationFrom;
    private final long parkingDurationTo;
    private final long maxWaitTimeFrom;
    private final long maxWaitTimeTo;
    private final TimeUnit timeUnit;

    public TrafficGenerator(ParkingLotManager parkingLotManager,
                            long intervalFrom, long intervalTo,
                            long parkingDurationFrom, long parkingDurationTo,
                            long maxWaitTimeFrom, long maxWaitTimeTo,
                            TimeUnit timeUnit) {
        this.parkingLotManager = parkingLotManager;
        this.intervalFrom = intervalFrom;
        this.intervalTo = intervalTo;
        this.parkingDurationFrom = parkingDurationFrom;
        this.parkingDurationTo = parkingDurationTo;
        this.maxWaitTimeFrom = maxWaitTimeFrom;
        this.maxWaitTimeTo = maxWaitTimeTo;
        this.timeUnit = timeUnit;
    }

    public void generate(long numberOfCars) {
        for (int i = 0; i < numberOfCars; i++) {
            Car car = new Car(i + 1, getRandomParkingDuration(), getRandomWaitTime(), timeUnit);
            new Thread(() -> {
                parkingLotManager.parkCar(car);
            }).start();
            try {
                timeUnit.sleep(getRandomInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getRandomParkingDuration() {
        return Math.round(Math.random() * (parkingDurationTo - parkingDurationFrom) + parkingDurationFrom);
    }

    private long getRandomWaitTime() {
        return Math.round(Math.random() * (maxWaitTimeTo - maxWaitTimeFrom) + maxWaitTimeFrom);
    }

    private long getRandomInterval() {
        return Math.round(Math.random() * (intervalTo - intervalFrom) + intervalFrom);
    }
}
