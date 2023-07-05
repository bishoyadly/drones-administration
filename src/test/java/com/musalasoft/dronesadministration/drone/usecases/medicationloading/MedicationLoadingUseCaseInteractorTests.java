package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationUseCaseErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationLoadingUseCaseInteractorTests {

    @Mock
    MedicationLoadingGateway gateway;

    @Mock
    MedicationLoadingOutputBoundary outputBoundary;

    MedicationLoadingMapper medicationLoadingMapper;

    MedicationLoadingInputBoundary useCaseInteractor;

    MedicationLoadingRequest request;
    MedicationLoadingResponse response;

    MedicationLoadingGatewayRequest gatewayRequest;
    MedicationLoadingGatewayResponse gatewayResponse;

    MedicationLoadingRequest buildMedicationLoadingRequest() {
        MedicationLoadingRequest request = new MedicationLoadingRequest();
        request.setDroneSerialNumber("ABC12345");
        request.setMedicationCodesList(List.of("KLM_123", "LMN_123"));
        return request;
    }

    MedicationLoadingResponse buildEmptyMedicationLoadingResponse() {
        MedicationLoadingResponse response = new MedicationLoadingResponse();
        response.setDroneSerialNumber("");
        response.setDroneModel("");
        response.setDroneLoadWeightLimitInGram(0);
        response.setDroneBatteryCapacityInPercentage(0);
        response.setDroneState("");
        response.setMedicationResponseList(new ArrayList<>());
        return response;
    }

    MedicationLoadingGatewayRequest buildMedicationLoadingGatewayRequest() {
        MedicationLoadingGatewayRequest gatewayRequest = new MedicationLoadingGatewayRequest();
        gatewayRequest.setDroneSerialNumber("ABC12345");
        gatewayRequest.setMedicationCodesList(List.of("ABC_123", "XYZ_123"));
        return gatewayRequest;
    }

    MedicationLoadingGatewayResponse buildMedicationLoadingGatewayResponse() {
        MedicationLoadingGatewayResponse gatewayResponse = new MedicationLoadingGatewayResponse();
        gatewayResponse.setDroneSerialNumber("ABC12345");
        gatewayResponse.setDroneLoadWeightLimitInGram(300);
        gatewayResponse.setDroneBatteryCapacityInPercentage(100);
        gatewayResponse.setDroneModel("HEAVY_WEIGHT");
        gatewayResponse.setDroneState("IDLE");
        MedicationGatewayResponse medicationGatewayResponse1 = buildMedicationGatewayResponse("ABC_123", "Medicine1");
        MedicationGatewayResponse medicationGatewayResponse2 = buildMedicationGatewayResponse("XYZ_123", "Medicine2");
        gatewayResponse.setMedicationGatewayResponseList(List.of(medicationGatewayResponse1, medicationGatewayResponse2));
        return gatewayResponse;
    }

    MedicationGatewayResponse buildMedicationGatewayResponse(String code, String name) {
        MedicationGatewayResponse gatewayResponse = new MedicationGatewayResponse();
        gatewayResponse.setCode(code);
        gatewayResponse.setName(name);
        gatewayResponse.setWeightInGram(50);
        gatewayResponse.setImageUrl("https://imageUrl.com");
        return gatewayResponse;
    }

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

    void verifyUpdateDroneRecordWithMedicationsArgument(MedicationLoadingGatewayRequest expectedGatewayRequest) {
        ArgumentCaptor<MedicationLoadingGatewayRequest> argumentCaptor = ArgumentCaptor.forClass(MedicationLoadingGatewayRequest.class);
        verify(gateway, times(1)).updateDroneRecordWithMedications(argumentCaptor.capture());
        assertMedicationLoadingGatewayRequest(expectedGatewayRequest, argumentCaptor.getValue());
    }

    void verifyGetDronesPaginatedArgument(MedicationLoadingGatewayRequest expectedGatewayRequest) {
        ArgumentCaptor<MedicationLoadingGatewayRequest> argumentCaptor = ArgumentCaptor.forClass(MedicationLoadingGatewayRequest.class);
        verify(gateway, times(1)).getDronesPaginated(argumentCaptor.capture());
        assertMedicationLoadingGatewayRequest(expectedGatewayRequest, argumentCaptor.getValue());
    }

    void verifyPresentSuccessListArgument(List<MedicationLoadingResponse> expectedGatewayRequestList) {
        ArgumentCaptor<MedicationLoadingResponsePage> argumentCaptor = ArgumentCaptor.forClass(MedicationLoadingResponsePage.class);
        verify(outputBoundary, times(1)).presentSuccessResponseList(argumentCaptor.capture());
        for (int i = 0; i < expectedGatewayRequestList.size(); i++) {
            assertResponse(expectedGatewayRequestList.get(i), argumentCaptor.getValue().getMedicationLoadingResponseList().get(i));
        }
    }

    void verifyPresentSuccessArgument(MedicationLoadingResponse expectedGatewayRequest) {
        ArgumentCaptor<MedicationLoadingResponse> argumentCaptor = ArgumentCaptor.forClass(MedicationLoadingResponse.class);
        verify(outputBoundary, times(1)).presentSuccessResponse(argumentCaptor.capture());
        assertResponse(expectedGatewayRequest, argumentCaptor.getValue());
    }

    void assertMedicationLoadingGatewayRequest(MedicationLoadingGatewayRequest expected, MedicationLoadingGatewayRequest actual) {
        assertEquals(expected.getDroneSerialNumber(), actual.getDroneSerialNumber());
        assertEquals(expected.getDroneState(), actual.getDroneState());
        assertEquals(expected.getMedicationCodesList(), actual.getMedicationCodesList());
    }

    void assertEmptyResponse(MedicationLoadingResponse response) {
        assertEquals("", response.getDroneSerialNumber());
        assertEquals("", response.getDroneModel());
        assertEquals(0, response.getDroneLoadWeightLimitInGram());
        assertEquals(0, response.getDroneBatteryCapacityInPercentage());
        assertEquals("", response.getDroneState());
        assertEquals(0, response.getMedicationResponseList().size());
    }

    void assertResponse(MedicationLoadingResponse expected, MedicationLoadingResponse actual) {
        assertEquals(expected.getDroneSerialNumber(), actual.getDroneSerialNumber());
        assertEquals(expected.getDroneModel(), actual.getDroneModel());
        assertEquals(expected.getDroneLoadWeightLimitInGram(), actual.getDroneLoadWeightLimitInGram());
        assertEquals(expected.getDroneBatteryCapacityInPercentage(), actual.getDroneBatteryCapacityInPercentage());
        assertEquals(expected.getDroneState(), actual.getDroneState());
        assertEquals(expected.getMedicationResponseList().size(), actual.getMedicationResponseList().size());
    }

    void assertResponsePageFields(MedicationLoadingResponsePage expected, MedicationLoadingResponsePage actual) {
        assertEquals(expected.getPageNumber(), actual.getPageNumber());
        assertEquals(expected.getPageSize(), actual.getPageSize());
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    void validateDronesAvailableForLoadingResponseList(List<MedicationLoadingResponse> responseList) {
        for (MedicationLoadingResponse response : responseList) {
            assertTrue(response.getDroneBatteryCapacityInPercentage() >= 25);
            Integer currentDroneLoad = 0;
            for (MedicationResponse medicationResponse : response.getMedicationResponseList()) {
                currentDroneLoad += medicationResponse.getWeightInGram();
            }
            assertTrue(response.getDroneLoadWeightLimitInGram() > currentDroneLoad);
        }
    }

    @BeforeEach
    void setUp() {
        medicationLoadingMapper = new MedicationLoadingMapperImpl();
        useCaseInteractor = new MedicationLoadingUseCaseInteractor(gateway, outputBoundary, medicationLoadingMapper);
        request = buildMedicationLoadingRequest();
        response = buildMedicationLoadingResponse();
        gatewayRequest = buildMedicationLoadingGatewayRequest();
        gatewayResponse = buildMedicationLoadingGatewayResponse();
    }

    @Test
    void loadDroneWithMedications_caseDroneDoesNotExist() {
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(false);
        when(outputBoundary.presentNotFoundFailureResponse(anyString())).thenReturn(buildEmptyMedicationLoadingResponse());

        MedicationLoadingResponse response = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(outputBoundary, times(1))
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
        assertEmptyResponse(response);
    }

    @Test
    void loadDroneWithMedications_caseMedicationDoesNotExist() {
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(gateway.doMedicationsExistByCodesList(anyList())).thenReturn(false);
        when(outputBoundary.presentNotFoundFailureResponse(anyString())).thenReturn(buildEmptyMedicationLoadingResponse());

        MedicationLoadingResponse response = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).doMedicationsExistByCodesList(request.getMedicationCodesList());
        verify(outputBoundary, times(1))
                .presentNotFoundFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        assertEmptyResponse(response);
    }

    @Test
    void loadDroneWithMedications_caseLoadExceedsDroneLoadWeightLimit() {
        gatewayResponse.setDroneLoadWeightLimitInGram(10);
        gatewayResponse.getMedicationGatewayResponseList().get(0).setWeightInGram(10);
        gatewayResponse.getMedicationGatewayResponseList().get(1).setWeightInGram(10);
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(gateway.doMedicationsExistByCodesList(anyList())).thenReturn(true);
        when(gateway.getDroneRecordBySerialNumber(anyString())).thenReturn(gatewayResponse);
        when(gateway.getMedicationRecordsByCodesList(anyList())).thenReturn(gatewayResponse.getMedicationGatewayResponseList());
        when(outputBoundary.presentBadRequestFailureResponse(anyString())).thenReturn(buildEmptyMedicationLoadingResponse());

        MedicationLoadingResponse response = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).doMedicationsExistByCodesList(request.getMedicationCodesList());
        verify(gateway, times(1)).getDroneRecordBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).getMedicationRecordsByCodesList(request.getMedicationCodesList());
        verify(outputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationLoadingErrorMessages.MEDICATION_LOAD_EXCEEDS_LOAD_LIMIT);
        assertEmptyResponse(response);
    }

    @Test
    void loadDroneWithMedications_caseDroneBatteryCapacityBelow25Percent() {
        gatewayResponse.setDroneBatteryCapacityInPercentage(24);
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(gateway.doMedicationsExistByCodesList(anyList())).thenReturn(true);
        when(gateway.getDroneRecordBySerialNumber(anyString())).thenReturn(gatewayResponse);
        when(outputBoundary.presentBadRequestFailureResponse(anyString())).thenReturn(buildEmptyMedicationLoadingResponse());

        MedicationLoadingResponse response = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).doMedicationsExistByCodesList(request.getMedicationCodesList());
        verify(gateway, times(1)).getDroneRecordBySerialNumber(request.getDroneSerialNumber());
        verify(outputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationLoadingErrorMessages.DRONE_BATTERY_CAPACITY_IS_LOW);
        assertEmptyResponse(response);
    }

    @Test
    void loadDroneWithMedications_caseMedicationAlreadyLoaded() {
        gatewayResponse.getMedicationGatewayResponseList().get(0).setCode(request.getMedicationCodesList().get(0));
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(gateway.doMedicationsExistByCodesList(anyList())).thenReturn(true);
        when(gateway.getDroneRecordBySerialNumber(anyString())).thenReturn(gatewayResponse);
        when(outputBoundary.presentBadRequestFailureResponse(anyString())).thenReturn(buildEmptyMedicationLoadingResponse());

        MedicationLoadingResponse response = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).doMedicationsExistByCodesList(request.getMedicationCodesList());
        verify(gateway, times(1)).getDroneRecordBySerialNumber(request.getDroneSerialNumber());
        verify(outputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationLoadingErrorMessages.MEDICATION_ALREADY_LOADED);
        assertEmptyResponse(response);
    }

    @Test
    void loadDroneWithMedications_caseValidRequest() {
        gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        gatewayRequest.setDroneState(DroneEntity.State.LOADING.name());
        response = medicationLoadingMapper.gatewayResponseToResponse(gatewayResponse);
        when(gateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(gateway.doMedicationsExistByCodesList(anyList())).thenReturn(true);
        when(gateway.getDroneRecordBySerialNumber(anyString())).thenReturn(gatewayResponse);
        when(gateway.getMedicationRecordsByCodesList(anyList())).thenReturn(gatewayResponse.getMedicationGatewayResponseList());
        when(gateway.updateDroneRecordWithMedications(any())).thenReturn(gatewayResponse);
        when(outputBoundary.presentSuccessResponse(any())).thenReturn(response);

        MedicationLoadingResponse actualResponse = (MedicationLoadingResponse) useCaseInteractor.loadDroneWithMedications(request);

        verify(gateway, times(1)).droneExistsBySerialNumber(request.getDroneSerialNumber());
        verify(gateway, times(1)).doMedicationsExistByCodesList(request.getMedicationCodesList());
        verifyUpdateDroneRecordWithMedicationsArgument(gatewayRequest);
        verifyPresentSuccessArgument(response);
        assertResponse(response, actualResponse);
    }

    @Test
    void getDronesAvailableForLoading() {
        request.setDronesPageNumber(0);
        request.setDronesPageSize(10);
        MedicationLoadingGatewayRequest gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        MedicationLoadingGatewayResponse gatewayResponse1 = buildMedicationLoadingGatewayResponse();
        gatewayResponse1.setDroneBatteryCapacityInPercentage(24);
        MedicationLoadingGatewayResponse gatewayResponse2 = buildMedicationLoadingGatewayResponse();
        gatewayResponse2.setDroneLoadWeightLimitInGram(100);
        MedicationLoadingGatewayResponse gatewayResponse3 = buildMedicationLoadingGatewayResponse();
        List<MedicationLoadingGatewayResponse> gatewayResponseList = List.of(gatewayResponse1, gatewayResponse2, gatewayResponse3);
        MedicationLoadingGatewayResponsePage expectedGatewayPage = new MedicationLoadingGatewayResponsePage();
        expectedGatewayPage.setPageNumber(0);
        expectedGatewayPage.setPageSize(10);
        expectedGatewayPage.setTotalElements(3);
        expectedGatewayPage.setMedicationLoadingGatewayResponseList(gatewayResponseList);
        List<MedicationLoadingResponse> expectedResponseList = medicationLoadingMapper
                .gatewayResponseListToResponseList(List.of(gatewayResponse3));
        MedicationLoadingResponsePage expectedResponsePage = new MedicationLoadingResponsePage();
        expectedResponsePage.setPageNumber(0);
        expectedResponsePage.setPageSize(10);
        expectedResponsePage.setTotalElements(3);
        expectedResponsePage.setMedicationLoadingResponseList(expectedResponseList);
        when(gateway.getDronesPaginated(any())).thenReturn(expectedGatewayPage);
        when(outputBoundary.presentSuccessResponseList(any())).thenReturn(expectedResponsePage);

        MedicationLoadingResponsePage actualResponsePage = (MedicationLoadingResponsePage)
                useCaseInteractor.getDronesAvailableForLoading(request);

        verifyGetDronesPaginatedArgument(gatewayRequest);
        verifyPresentSuccessListArgument(expectedResponseList);
        assertResponsePageFields(expectedResponsePage, actualResponsePage);
        validateDronesAvailableForLoadingResponseList(expectedResponsePage.getMedicationLoadingResponseList());
    }

    @Test
    void getDronesPage() {
        request.setDronesPageNumber(0);
        request.setDronesPageSize(10);
        MedicationLoadingGatewayRequest gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        MedicationLoadingGatewayResponse gatewayResponse1 = buildMedicationLoadingGatewayResponse();
        MedicationLoadingGatewayResponse gatewayResponse2 = buildMedicationLoadingGatewayResponse();
        MedicationLoadingGatewayResponse gatewayResponse3 = buildMedicationLoadingGatewayResponse();
        List<MedicationLoadingGatewayResponse> gatewayResponseList = List.of(gatewayResponse1, gatewayResponse2, gatewayResponse3);
        List<MedicationLoadingResponse> expectedResponseList = medicationLoadingMapper
                .gatewayResponseListToResponseList(List.of(gatewayResponse1, gatewayResponse2, gatewayResponse3));
        MedicationLoadingGatewayResponsePage expectedGatewayPage = new MedicationLoadingGatewayResponsePage();
        expectedGatewayPage.setPageNumber(0);
        expectedGatewayPage.setPageSize(10);
        expectedGatewayPage.setTotalElements(3);
        expectedGatewayPage.setMedicationLoadingGatewayResponseList(gatewayResponseList);
        MedicationLoadingResponsePage expectedResponsePage = new MedicationLoadingResponsePage();
        expectedResponsePage.setPageNumber(0);
        expectedResponsePage.setPageSize(10);
        expectedResponsePage.setTotalElements(3);
        expectedResponsePage.setMedicationLoadingResponseList(expectedResponseList);
        when(gateway.getDronesPaginated(any())).thenReturn(expectedGatewayPage);
        when(outputBoundary.presentSuccessResponseList(any())).thenReturn(expectedResponsePage);

        MedicationLoadingResponsePage actualResponsePage = (MedicationLoadingResponsePage) useCaseInteractor.getDronesPage(request);

        verifyGetDronesPaginatedArgument(gatewayRequest);
        verifyPresentSuccessListArgument(expectedResponseList);
        assertResponsePageFields(expectedResponsePage, actualResponsePage);
    }

}
