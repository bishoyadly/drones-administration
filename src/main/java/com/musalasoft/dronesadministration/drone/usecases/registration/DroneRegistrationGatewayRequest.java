package com.musalasoft.dronesadministration.drone.usecases.registration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneRegistrationGatewayRequest {
    private String serialNumber;
    private Integer loadWeightLimitInGram;
    private Integer batteryCapacityInPercentage;
    private String model;
    private String state;
}
