package com.musalasoft.dronesadministration.drone.usecases.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DroneUseCaseInteractorsFactory {

    public static DroneRegistrationUseCaseInteractor buildDroneRegistrationUseCaseInteractor(DroneRegistrationGateway registrationGateway,
                                                                                             DroneRegistrationOutputBoundary registrationOutputBoundary) {
        DroneMapper droneMapper = new DroneMapperImpl();
        return new DroneRegistrationUseCaseInteractor(registrationGateway, registrationOutputBoundary, droneMapper);
    }

}
