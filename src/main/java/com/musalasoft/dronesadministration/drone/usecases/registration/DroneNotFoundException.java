package com.musalasoft.dronesadministration.drone.usecases.registration;

public class DroneNotFoundException extends RuntimeException {
    public DroneNotFoundException(String message) {
        super(message);
    }
}
