package com.musalasoft.dronesadministration.drone.usecases.registration;

public class DroneBadRequestException extends RuntimeException {
    public DroneBadRequestException(String message) {
        super(message);
    }
}
