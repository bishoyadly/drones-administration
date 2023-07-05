package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationLoadingRequest {
    String droneSerialNumber;
    List<String> medicationCodesList;
    Integer dronesPageSize;
    Integer dronesPageNumber;
}
