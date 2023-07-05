package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationLoadingResponse {
    private String droneSerialNumber;
    private Integer droneLoadWeightLimitInGram;
    private Integer droneBatteryCapacityInPercentage;
    private String droneModel;
    private String droneState;
    private List<MedicationResponse> medicationResponseList;
}
