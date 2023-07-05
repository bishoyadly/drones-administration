package com.musalasoft.dronesadministration.medication.usecases;

public interface MedicationInputBoundary {
    Object addMedication(MedicationRequest medicationRequest);

    Object updateMedication(MedicationRequest medicationRequest);

    Object getMedicationByCode(String code);

    Object deleteMedicationByCode(String code);
}
