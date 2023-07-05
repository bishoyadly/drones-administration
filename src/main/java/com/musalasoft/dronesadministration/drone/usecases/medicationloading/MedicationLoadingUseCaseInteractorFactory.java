package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationLoadingUseCaseInteractorFactory {

    public static MedicationLoadingUseCaseInteractor buildMedicationLoadingUseCaseInteractor(MedicationLoadingGateway gateway,
                                                                                             MedicationLoadingOutputBoundary outputBoundary) {
        MedicationLoadingMapper mapper = new MedicationLoadingMapperImpl();
        return new MedicationLoadingUseCaseInteractor(gateway, outputBoundary, mapper);
    }

}
