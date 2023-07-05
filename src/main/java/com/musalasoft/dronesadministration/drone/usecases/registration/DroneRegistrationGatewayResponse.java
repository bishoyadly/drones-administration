package com.musalasoft.dronesadministration.drone.usecases.registration;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DroneRegistrationGatewayResponse {
    private String serialNumber;
    private Integer loadWeightLimitInGram;
    private Integer batteryCapacityInPercentage;
    private String model;
    private String state;
    private List<MedicationGatewayResponse> medicationGatewayResponseList;
}
