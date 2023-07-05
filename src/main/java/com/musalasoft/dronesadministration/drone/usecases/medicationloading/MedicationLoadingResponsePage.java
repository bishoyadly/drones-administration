package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationLoadingResponsePage {
    Integer pageNumber;
    Integer pageSize;
    Integer totalElements;
    List<MedicationLoadingResponse> medicationLoadingResponseList;
}
