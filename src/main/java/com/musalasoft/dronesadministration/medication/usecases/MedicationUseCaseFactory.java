package com.musalasoft.dronesadministration.medication.usecases;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationUseCaseFactory {
    public static MedicationInputBoundary buildMedicationInputBoundary(MedicationGateway gateway,
                                                                       MedicationOutputBoundary outputBoundary) {
        MedicationMapper medicationMapper = new MedicationMapperImpl();
        return new MedicationUseCaseInteractor(gateway, outputBoundary, medicationMapper);
    }
}
