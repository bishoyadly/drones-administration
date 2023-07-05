package com.musalasoft.dronesadministration.drone.usecases.registration;

public interface DroneRegistrationOutputBoundary {
    Object presentSuccessResponse(DroneRegistrationResponse response);

    Object presentSuccessEmptyResponse();

    Object presentBadRequestFailureResponse(String errorMessage);

    Object presentNotFoundFailureResponse(String errorMessage);
}
