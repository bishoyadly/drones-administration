package com.musalasoft.dronesadministration.drone.usecases.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DroneRegistrationUseCaseErrorMessages {
    public static final String DRONE_ALREADY_REGISTERED = "Drone Already Registered";
    public static final String DRONE_DOES_NOT_EXIST = "Drone Does Not Exist";
}
