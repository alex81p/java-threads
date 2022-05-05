package com.example.model;

public class ParkingSpot {
    private final int id;
    private volatile boolean isBusy;

    public ParkingSpot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}
