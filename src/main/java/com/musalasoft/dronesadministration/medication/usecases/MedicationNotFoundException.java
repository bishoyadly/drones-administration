package com.musalasoft.dronesadministration.medication.usecases;

public class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) {
        super(message);
    }
}
