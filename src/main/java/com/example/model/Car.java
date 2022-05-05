package com.example.model;

import java.util.concurrent.TimeUnit;

public class Car {
    private final long id;
    private final long parkingDuration;
    private final long maxWaitTime;
    private final TimeUnit timeUnit;

    public Car(long id, long parkingDuration, long maxWaitTime, TimeUnit timeUnit) {
        this.id = id;
        this.parkingDuration = parkingDuration;
        this.maxWaitTime = maxWaitTime;
        this.timeUnit = timeUnit;
    }

    public long getId() {
        return id;
    }

    public long getParkingDuration() {
        return parkingDuration;
    }

    public long getMaxWaitTime() {
        return maxWaitTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}