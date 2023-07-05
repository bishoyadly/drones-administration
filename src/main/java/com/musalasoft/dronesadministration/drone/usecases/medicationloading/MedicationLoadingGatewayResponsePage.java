package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationLoadingGatewayResponsePage {
    Integer pageNumber;
    Integer pageSize;
    Integer totalElements;
    List<MedicationLoadingGatewayResponse> medicationLoadingGatewayResponseList;
}
