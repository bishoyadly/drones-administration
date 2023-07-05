package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationInputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicationComponentTests {

    @Mock
    MedicationInputBoundary medicationInputBoundary;

    MedicationComponent medicationComponent;

    @BeforeEach
    void setUp() {
        medicationComponent = new MedicationServiceComponent(medicationInputBoundary);
    }

    @Test
    void addMedication() {
        MedicationRequest request = new MedicationRequest();
        medicationComponent.addMedication(request);
        verify(medicationInputBoundary, times(1)).addMedication(request);
    }

    @Test
    void updateMedication() {
        MedicationRequest request = new MedicationRequest();
        medicationComponent.updateMedication(request);
        verify(medicationInputBoundary, times(1)).updateMedication(request);
    }

    @Test
    void getMedicationByCode() {
        String medicationCode = "ABC_123";
        medicationComponent.getMedicationByCode(medicationCode);
        verify(medicationInputBoundary, times(1)).getMedicationByCode(medicationCode);
    }

    @Test
    void deleteMedicationByCode() {
        String medicationCode = "ABC_123";
        medicationComponent.deleteMedicationByCode(medicationCode);
        verify(medicationInputBoundary, times(1)).deleteMedicationByCode(medicationCode);
    }

}
