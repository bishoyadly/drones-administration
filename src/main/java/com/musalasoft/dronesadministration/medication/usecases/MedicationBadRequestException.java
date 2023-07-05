package com.musalasoft.dronesadministration.medication.usecases;

public class MedicationBadRequestException extends RuntimeException {
    public MedicationBadRequestException(String message) {
        super(message);
    }
}
