package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGateway;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingUseCaseInteractorFactory;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGateway;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneUseCaseInteractorsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroneComponentSetup {

    @Bean
    public DroneRegistrationInputBoundary droneRegistrationInputBoundary(DroneRegistrationGateway droneRegistrationGateway,
                                                                         DroneRegistrationOutputBoundary droneRegistrationOutputBoundary) {
        return DroneUseCaseInteractorsFactory.buildDroneRegistrationUseCaseInteractor(droneRegistrationGateway, droneRegistrationOutputBoundary);
    }

    @Bean
    public MedicationLoadingInputBoundary medicationLoadingInputBoundary(MedicationLoadingGateway medicationLoadingGateway,
                                                                         MedicationLoadingOutputBoundary medicationLoadingOutputBoundary) {
        return MedicationLoadingUseCaseInteractorFactory.buildMedicationLoadingUseCaseInteractor(medicationLoadingGateway, medicationLoadingOutputBoundary);
    }
}
