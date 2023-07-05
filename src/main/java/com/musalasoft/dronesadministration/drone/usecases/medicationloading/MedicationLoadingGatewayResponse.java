package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationLoadingGatewayResponse {
    private String droneSerialNumber;
    private Integer droneLoadWeightLimitInGram;
    private Integer droneBatteryCapacityInPercentage;
    private String droneModel;
    private String droneState;
    private List<MedicationGatewayResponse> medicationGatewayResponseList;
}
