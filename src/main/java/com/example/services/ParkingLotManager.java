package com.example.services;

import com.example.exceptions.ParkingLotException;
import com.example.model.Car;
import com.example.model.ParkingLot;
import com.example.model.ParkingSpot;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLotManager {
    private final ParkingLot parkingLot;
    private final Semaphore semaphore;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingLotManager(ParkingLot parkingLot, boolean isFair) {
        this.parkingLot = parkingLot;
        this.semaphore = new Semaphore(parkingLot.getParkingSpots().size(), isFair);
    }

    public void parkCar(Car car) {
        System.out.printf("%s Car #%d (parkingDuration = %d, maxWaitTime = %d) started searching for an empty parking spot\n",
                now(), car.getId(), car.getParkingDuration(), car.getMaxWaitTime());
        try {
            if (semaphore.tryAcquire(car.getMaxWaitTime(), car.getTimeUnit())) {
                ParkingSpot parkingSpot;
                try {
                    lock.lock();
                    parkingSpot = parkingLot.getParkingSpots().stream()
                            .filter(o -> !o.isBusy())
                            .findFirst()
                            .orElseThrow(() -> new ParkingLotException("Unable to find an empty spot"));
                    parkingSpot.setBusy(true);
                } finally {
                    lock.unlock();
                }
                System.out.printf("%s Spot #%d: Car #%d started parking\n", now(), parkingSpot.getId(), car.getId());
                try {
                    car.getTimeUnit().sleep(car.getParkingDuration());
                } finally {
                    parkingSpot.setBusy(false);
                    semaphore.release();
                }
                System.out.printf("%s Spot #%d: Car #%d finished parking\n", now(), parkingSpot.getId(), car.getId());
            } else {
                System.out.printf("%s Car #%d stopped searching due to timeout", now(), car.getId());
            }
        } catch (InterruptedException e) {
            throw new ParkingLotException("Car #" + car.getId() + " interrupted parking", e);
        }
    }

    private String now() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
