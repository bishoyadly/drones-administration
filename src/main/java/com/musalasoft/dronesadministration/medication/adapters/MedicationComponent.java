package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;

public interface MedicationComponent {

    MedicationResponse addMedication(MedicationRequest medicationRequest);

    MedicationResponse updateMedication(MedicationRequest medicationRequest);

    MedicationResponse getMedicationByCode(String code);

    MedicationResponse deleteMedicationByCode(String code);
}
