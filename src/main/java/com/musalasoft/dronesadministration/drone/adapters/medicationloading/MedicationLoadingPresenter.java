package com.musalasoft.dronesadministration.drone.adapters.medicationloading;

import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MedicationLoadingPresenter implements MedicationLoadingOutputBoundary {

    @Override
    public Object presentSuccessResponse(MedicationLoadingResponse response) {
        return response;
    }

    @Override
    public Object presentSuccessResponseList(MedicationLoadingResponsePage responsePage) {
        return responsePage;
    }

    @Override
    public Object presentNotFoundFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
        problemDto.setHttpStatusMessage(HttpStatus.NOT_FOUND.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new DroneComponentException(problemDto);
    }

    @Override
    public Object presentBadRequestFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        problemDto.setHttpStatusMessage(HttpStatus.BAD_REQUEST.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new DroneComponentException(problemDto);
    }
}
