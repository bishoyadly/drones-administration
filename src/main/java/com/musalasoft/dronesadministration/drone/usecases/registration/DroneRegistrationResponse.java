package com.musalasoft.dronesadministration.drone.usecases.registration;

import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DroneRegistrationResponse {
    private String serialNumber;
    private Integer loadWeightLimitInGram;
    private Integer batteryCapacityInPercentage;
    private String model;
    private String state;
    private List<MedicationResponse> medicationResponseList;
}
