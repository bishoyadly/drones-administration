package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationOutputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicationPresenterTests {

    MedicationOutputBoundary medicationOutputBoundary;

    MedicationResponse buildMedicationResponse() {
        MedicationResponse request = new MedicationResponse();
        request.setCode("ABC_123");
        request.setName("Medicine-123_1mg");
        request.setWeightInGram(10);
        request.setImageUrl("https://test-image-url.com");
        return request;
    }

    void assertMedicationResponseFields(MedicationResponse expected, MedicationResponse actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWeightInGram(), actual.getWeightInGram());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    private void assertBadRequestProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            medicationOutputBoundary.presentBadRequestFailureResponse(expectedErrorMessage);
        } catch (MedicationComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    private void assertNotFoundProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            medicationOutputBoundary.presentNotFoundFailureResponse(expectedErrorMessage);
        } catch (MedicationComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    void assertNullMedicationResponse(MedicationResponse response) {
        assertEquals("", response.getCode());
        assertEquals("", response.getName());
        assertEquals(0, response.getWeightInGram());
        assertEquals("", response.getImageUrl());
    }

    @BeforeEach
    void setUp() {
        medicationOutputBoundary = new MedicationPresenter();
    }

    @Test
    void presentMedicationSuccessResponse() {
        MedicationResponse expectedResponse = buildMedicationResponse();
        MedicationResponse actualResponse = (MedicationResponse) medicationOutputBoundary.presentMedicationSuccessResponse(expectedResponse);
        assertMedicationResponseFields(expectedResponse, actualResponse);
    }

    @Test
    void presentMedicationSuccessEmptyResponse() {
        MedicationResponse actualResponse = (MedicationResponse) medicationOutputBoundary.presentMedicationSuccessEmptyResponse();
        assertNullMedicationResponse(actualResponse);
    }

    @Test
    void presentBadRequestFailureResponse() {
        assertThrows(MedicationComponentException.class, () ->
                medicationOutputBoundary.presentBadRequestFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_ALREADY_EXISTS));
        assertBadRequestProblemDto(HttpStatus.BAD_REQUEST, MedicationUseCaseErrorMessages.MEDICATION_ALREADY_EXISTS);
    }

    @Test
    void presentNotFoundFailureResponse() {
        assertThrows(MedicationComponentException.class, () ->
                medicationOutputBoundary.presentNotFoundFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
        assertNotFoundProblemDto(HttpStatus.NOT_FOUND, MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
    }

}
