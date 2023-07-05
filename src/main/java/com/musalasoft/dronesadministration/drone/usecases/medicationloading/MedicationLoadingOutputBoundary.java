package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

public interface MedicationLoadingOutputBoundary {
    Object presentSuccessResponse(MedicationLoadingResponse response);

    Object presentSuccessResponseList(MedicationLoadingResponsePage responsePage);

    Object presentNotFoundFailureResponse(String errorMessage);

    Object presentBadRequestFailureResponse(String errorMessage);
}
