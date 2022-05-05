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
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + " Car #" + car.getId() + " (parkingDuration = " + car.getParkingDuration()
                + ", maxWaitTime = " + car.getMaxWaitTime()
                + ") started searching for an empty parking spot");
        try {
            if (semaphore.tryAcquire(car.getMaxWaitTime(), car.getTimeUnit())) {
                lock.lock();
                ParkingSpot parkingSpot = parkingLot.getParkingSpots().stream()
                        .filter(o -> !o.isBusy())
                        .findFirst()
                        .get();
                parkingSpot.setBusy(true);
                lock.unlock();
                System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        + " Car #" + car.getId() + " started parking");
                try {
                    car.getTimeUnit().sleep(car.getParkingDuration());
                } finally {
                    parkingSpot.setBusy(false);
                    semaphore.release();
                }
                System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        + " Car #" + car.getId() + " finished parking");
            } else {
                System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        + " Car #" + car.getId() + " stopped searching due to timeout");
            }
        } catch (InterruptedException e) {
            throw new ParkingLotException("Car #" + car.getId() + " interrupted parking", e);
        }
    }
}
