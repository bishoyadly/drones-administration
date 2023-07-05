package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationInputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicationServiceComponent implements MedicationComponent {

    private final MedicationInputBoundary medicationInputBoundary;

    @Autowired
    public MedicationServiceComponent(MedicationInputBoundary medicationInputBoundary) {
        this.medicationInputBoundary = medicationInputBoundary;
    }

    @Override
    public MedicationResponse addMedication(MedicationRequest medicationRequest) {
        return (MedicationResponse) medicationInputBoundary.addMedication(medicationRequest);
    }

    @Override
    public MedicationResponse updateMedication(MedicationRequest medicationRequest) {
        return (MedicationResponse) medicationInputBoundary.updateMedication(medicationRequest);
    }

    @Override
    public MedicationResponse getMedicationByCode(String code) {
        return (MedicationResponse) medicationInputBoundary.getMedicationByCode(code);
    }

    @Override
    public MedicationResponse deleteMedicationByCode(String code) {
        return (MedicationResponse) medicationInputBoundary.deleteMedicationByCode(code);
    }
}
