package com.musalasoft.dronesadministration.drone.adapters.registration;

import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class DroneRegistrationPresenter implements DroneRegistrationOutputBoundary {

    @Override
    public Object presentSuccessResponse(DroneRegistrationResponse response) {
        return response;
    }

    @Override
    public Object presentSuccessEmptyResponse() {
        return buildEmptyDroneRegistrationResponse();
    }

    @Override
    public Object presentBadRequestFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        problemDto.setHttpStatusMessage(HttpStatus.BAD_REQUEST.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new DroneComponentException(problemDto);
    }

    @Override
    public Object presentNotFoundFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
        problemDto.setHttpStatusMessage(HttpStatus.NOT_FOUND.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new DroneComponentException(problemDto);
    }

    DroneRegistrationResponse buildEmptyDroneRegistrationResponse() {
        DroneRegistrationResponse response = new DroneRegistrationResponse();
        response.setSerialNumber("");
        response.setModel("");
        response.setLoadWeightLimitInGram(0);
        response.setBatteryCapacityInPercentage(0);
        response.setState("");
        return response;
    }
}
