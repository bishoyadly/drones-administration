package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationOutputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MedicationPresenter implements MedicationOutputBoundary {

    @Override
    public Object presentMedicationSuccessResponse(MedicationResponse response) {
        return response;
    }

    @Override
    public Object presentMedicationSuccessEmptyResponse() {
        return buildEmptyResponse();
    }

    @Override
    public Object presentBadRequestFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        problemDto.setHttpStatusMessage(HttpStatus.BAD_REQUEST.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new MedicationComponentException(problemDto);
    }

    @Override
    public Object presentNotFoundFailureResponse(String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
        problemDto.setHttpStatusMessage(HttpStatus.NOT_FOUND.toString());
        problemDto.setDetailedErrorReason(errorMessage);
        throw new MedicationComponentException(problemDto);
    }

    private MedicationResponse buildEmptyResponse() {
        MedicationResponse response = new MedicationResponse();
        response.setCode("");
        response.setName("");
        response.setWeightInGram(0);
        response.setImageUrl("");
        return response;
    }
}
