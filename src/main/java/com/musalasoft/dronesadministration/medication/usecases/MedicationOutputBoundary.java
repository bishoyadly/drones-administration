package com.musalasoft.dronesadministration.medication.usecases;

public interface MedicationOutputBoundary {

    Object presentMedicationSuccessResponse(MedicationResponse response);

    Object presentMedicationSuccessEmptyResponse();

    Object presentBadRequestFailureResponse(String errorMessage);

    Object presentNotFoundFailureResponse(String errorMessage);
}
