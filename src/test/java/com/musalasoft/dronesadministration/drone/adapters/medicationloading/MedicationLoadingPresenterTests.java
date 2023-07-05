package com.musalasoft.dronesadministration.drone.adapters.medicationloading;

import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingErrorMessages;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingOutputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.model.ProblemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicationLoadingPresenterTests {

    MedicationLoadingOutputBoundary outputBoundary;

    MedicationLoadingResponse buildMedicationLoadingResponse() {
        MedicationLoadingResponse response = new MedicationLoadingResponse();
        response.setDroneSerialNumber("ABC12345");
        response.setDroneLoadWeightLimitInGram(300);
        response.setDroneBatteryCapacityInPercentage(100);
        response.setDroneModel("HEAVY_WEIGHT");
        response.setDroneState("IDLE");
        MedicationResponse medicationResponse1 = buildMedicationResponse("ABC_123", "Medicine1");
        MedicationResponse medicationResponse2 = buildMedicationResponse("XYZ_123", "Medicine2");
        response.setMedicationResponseList(List.of(medicationResponse1, medicationResponse2));
        return response;
    }

    MedicationResponse buildMedicationResponse(String code, String name) {
        MedicationResponse gatewayResponse = new MedicationResponse();
        gatewayResponse.setCode(code);
        gatewayResponse.setName(name);
        gatewayResponse.setWeightInGram(50);
        gatewayResponse.setImageUrl("https://imageUrl.com");
        return gatewayResponse;
    }

    void assertResponsePage(MedicationLoadingResponsePage expected, MedicationLoadingResponsePage actual) {
        assertEquals(expected.getPageNumber(), actual.getPageNumber());
        assertEquals(expected.getPageSize(), actual.getPageSize());
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        for (int i = 0; i < expected.getMedicationLoadingResponseList().size(); i++) {
            assertResponse(expected.getMedicationLoadingResponseList().get(i), expected.getMedicationLoadingResponseList().get(i));
        }
    }


    void assertResponse(MedicationLoadingResponse expected, MedicationLoadingResponse actual) {
        assertEquals(expected.getDroneSerialNumber(), actual.getDroneSerialNumber());
        assertEquals(expected.getDroneModel(), actual.getDroneModel());
        assertEquals(expected.getDroneLoadWeightLimitInGram(), actual.getDroneLoadWeightLimitInGram());
        assertEquals(expected.getDroneBatteryCapacityInPercentage(), actual.getDroneBatteryCapacityInPercentage());
        assertEquals(expected.getDroneState(), actual.getDroneState());
        assertEquals(expected.getMedicationResponseList().size(), actual.getMedicationResponseList().size());
    }

    private void assertNotFoundProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            outputBoundary.presentNotFoundFailureResponse(expectedErrorMessage);
        } catch (DroneComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    private void assertBadRequestProblemDto(HttpStatus httpStatus, String expectedErrorMessage) {
        ProblemDto problemDto = new ProblemDto();
        try {
            outputBoundary.presentBadRequestFailureResponse(expectedErrorMessage);
        } catch (DroneComponentException e) {
            problemDto = e.getProblemDto();
        }
        assertEquals(httpStatus.value(), problemDto.getHttpStatusCode());
        assertEquals(httpStatus.toString(), problemDto.getHttpStatusMessage());
        assertEquals(expectedErrorMessage, problemDto.getDetailedErrorReason());
    }

    @BeforeEach
    void setUp() {
        outputBoundary = new MedicationLoadingPresenter();
    }

    @Test
    void presentSuccessResponse() {
        MedicationLoadingResponse expectedResponse = buildMedicationLoadingResponse();
        MedicationLoadingResponse actualResponse = (MedicationLoadingResponse) outputBoundary.presentSuccessResponse(expectedResponse);
        assertResponse(expectedResponse, actualResponse);
    }


    @Test
    void presentSuccessResponseList() {
        MedicationLoadingResponse expectedResponse = buildMedicationLoadingResponse();
        List<MedicationLoadingResponse> expectedResponseList = List.of(expectedResponse);
        MedicationLoadingResponsePage expectedResponsePage = new MedicationLoadingResponsePage();
        expectedResponsePage.setPageNumber(0);
        expectedResponsePage.setPageSize(10);
        expectedResponsePage.setTotalElements(20);
        expectedResponsePage.setMedicationLoadingResponseList(expectedResponseList);
        MedicationLoadingResponsePage actualResponsePage = (MedicationLoadingResponsePage) outputBoundary
                .presentSuccessResponseList(expectedResponsePage);
        assertResponsePage(expectedResponsePage, actualResponsePage);
    }

    @Test
    void presentNotFoundFailureResponse() {
        assertThrows(DroneComponentException.class, () -> outputBoundary
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
        assertNotFoundProblemDto(HttpStatus.NOT_FOUND, DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }

    @Test
    void presentBadRequestFailureResponse() {
        assertThrows(DroneComponentException.class, () -> outputBoundary
                .presentBadRequestFailureResponse(MedicationLoadingErrorMessages.MEDICATION_LOAD_EXCEEDS_LOAD_LIMIT));
        assertBadRequestProblemDto(HttpStatus.BAD_REQUEST, MedicationLoadingErrorMessages.MEDICATION_LOAD_EXCEEDS_LOAD_LIMIT);
    }
}
