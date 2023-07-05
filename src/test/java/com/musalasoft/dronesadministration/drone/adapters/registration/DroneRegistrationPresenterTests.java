package com.musalasoft.dronesadministration.drone.adapters.registration;

import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DroneRegistrationPresenterTests {

    DroneRegistrationOutputBoundary droneRegistrationOutputBoundary;

    DroneRegistrationResponse buildDroneRegistrationResponse() {
        DroneRegistrationResponse response = new DroneRegistrationResponse();
        response.setSerialNumber("ABCDE123");
        response.setModel("HEAVY_WEIGHT");
        response.setLoadWeightLimitInGram(300);
        response.setBatteryCapacityInPercentage(100);
        response.setState("IDLE");
        return response;
    }

    void assertDroneRegistrationResponseFields(DroneRegistrationResponse expected, DroneRegistrationResponse actual) {
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getLoadWeightLimitInGram(), actual.getLoadWeightLimitInGram());
        assertEquals(expected.getBatteryCapacityInPercentage(), actual.getBatteryCapacityInPercentage());
        assertEquals(expected.getState(), actual.getState());
    }

    void assertEmptyDroneRegistrationResponse(DroneRegistrationResponse response) {
        assertEquals("", response.getSerialNumber());
        assertEquals("", response.getModel());
        assertEquals("", response.getState());
        assertEquals(0, response.getLoadWeightLimitInGram());
        assertEquals(0, response.getBatteryCapacityInPercentage());
    }

    private void assertBadRequestProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            droneRegistrationOutputBoundary.presentBadRequestFailureResponse(expectedErrorMessage);
        } catch (DroneComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    private void assertNotFoundProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            droneRegistrationOutputBoundary.presentNotFoundFailureResponse(expectedErrorMessage);
        } catch (DroneComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    @BeforeEach
    void setUp() {
        droneRegistrationOutputBoundary = new DroneRegistrationPresenter();
    }

    @Test
    void presentSuccessResponse() {
        DroneRegistrationResponse expectedResponse = buildDroneRegistrationResponse();
        DroneRegistrationResponse actualResponse = (DroneRegistrationResponse) droneRegistrationOutputBoundary
                .presentSuccessResponse(expectedResponse);
        assertDroneRegistrationResponseFields(expectedResponse, actualResponse);
    }

    @Test
    void presentSuccessEmptyResponse() {
        DroneRegistrationResponse registrationResponse = (DroneRegistrationResponse) droneRegistrationOutputBoundary.presentSuccessEmptyResponse();
        assertEmptyDroneRegistrationResponse(registrationResponse);
    }

    @Test
    void presentBadRequestFailureResponse() {
        assertThrows(DroneComponentException.class, () -> droneRegistrationOutputBoundary
                .presentBadRequestFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_ALREADY_REGISTERED));
        assertBadRequestProblemDto(HttpStatus.BAD_REQUEST, DroneRegistrationUseCaseErrorMessages.DRONE_ALREADY_REGISTERED);
    }

    @Test
    void presentNotFoundFailureResponse() {
        assertThrows(DroneComponentException.class, () -> droneRegistrationOutputBoundary
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
        assertNotFoundProblemDto(HttpStatus.NOT_FOUND, DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }
}
