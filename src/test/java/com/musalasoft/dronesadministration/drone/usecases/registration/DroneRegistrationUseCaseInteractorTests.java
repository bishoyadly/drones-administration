package com.musalasoft.dronesadministration.drone.usecases.registration;

import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.entities.DroneErrorMessages;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneRegistrationUseCaseInteractorTests {

    @Mock
    DroneRegistrationOutputBoundary registrationOutputBoundary;

    @Mock
    DroneRegistrationGateway registrationGateway;

    DroneMapper droneMapper;

    DroneRegistrationInputBoundary registrationInputBoundary;

    DroneRegistrationRequest droneRegistrationRequest;

    DroneRegistrationResponse droneRegistrationResponse;

    DroneRegistrationRequest buildDroneRegistrationRequest() {
        DroneRegistrationRequest request = new DroneRegistrationRequest();
        request.setSerialNumber("ABCDE123");
        request.setModel("HEAVY_WEIGHT");
        request.setLoadWeightLimitInGram(300);
        request.setBatteryCapacityInPercentage(100);
        request.setState("IDLE");
        return request;
    }

    DroneRegistrationResponse buildDroneRegistrationResponse() {
        DroneRegistrationResponse response = new DroneRegistrationResponse();
        response.setSerialNumber("ABCDE123");
        response.setModel("HEAVY_WEIGHT");
        response.setLoadWeightLimitInGram(300);
        response.setBatteryCapacityInPercentage(100);
        response.setState("IDLE");
        List<MedicationResponse> medicationResponseList = new LinkedList<>();
        medicationResponseList.add(0, buildMedicationResponse("ABC_123", "medicine1"));
        medicationResponseList.add(1, buildMedicationResponse("XYZ_123", "medicine2"));
        response.setMedicationResponseList(medicationResponseList);
        return response;
    }

    MedicationResponse buildMedicationResponse(String code, String name) {
        MedicationResponse response = new MedicationResponse();
        response.setCode(code);
        response.setName(name);
        response.setWeightInGram(10);
        response.setImageUrl("https://imageUrl.com");
        return response;
    }

    DroneRegistrationGatewayRequest buildDroneRegistrationGatewayRequest() {
        DroneRegistrationGatewayRequest request = new DroneRegistrationGatewayRequest();
        request.setSerialNumber("ABCDE123");
        request.setModel("HEAVY_WEIGHT");
        request.setLoadWeightLimitInGram(300);
        request.setBatteryCapacityInPercentage(100);
        return request;
    }

    DroneRegistrationGatewayResponse buildDroneRegistrationGatewayResponse() {
        DroneRegistrationGatewayResponse request = new DroneRegistrationGatewayResponse();
        request.setSerialNumber("ABCDE123");
        request.setModel("HEAVY_WEIGHT");
        request.setLoadWeightLimitInGram(300);
        request.setBatteryCapacityInPercentage(100);
        return request;
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

    void verifyPresentSuccessResponseArgument(DroneRegistrationResponse expectedResponse) {
        ArgumentCaptor<DroneRegistrationResponse> argumentCaptor = ArgumentCaptor.forClass(DroneRegistrationResponse.class);
        verify(registrationOutputBoundary, times(1)).presentSuccessResponse(argumentCaptor.capture());
        assertDroneRegistrationResponseFields(expectedResponse, argumentCaptor.getValue());
    }

    void verifySaveDroneRecordArgument(DroneRegistrationGatewayRequest gatewayRequest) {
        ArgumentCaptor<DroneRegistrationGatewayRequest> argumentCaptor = ArgumentCaptor.forClass(DroneRegistrationGatewayRequest.class);
        verify(registrationGateway, times(1)).saveDroneRecord(argumentCaptor.capture());
        assertDroneRegistrationGatewayRequestFields(gatewayRequest, argumentCaptor.getValue());
    }

    void assertDroneRegistrationGatewayRequestFields(DroneRegistrationGatewayRequest expected, DroneRegistrationGatewayRequest actual) {
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getLoadWeightLimitInGram(), actual.getLoadWeightLimitInGram());
        assertEquals(expected.getBatteryCapacityInPercentage(), actual.getBatteryCapacityInPercentage());
        assertEquals(expected.getState(), actual.getState());
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

    @BeforeEach
    void setUp() {
        droneMapper = new DroneMapperImpl();
        registrationInputBoundary = new DroneRegistrationUseCaseInteractor(registrationGateway, registrationOutputBoundary, droneMapper);
        droneRegistrationRequest = buildDroneRegistrationRequest();
        droneRegistrationResponse = buildDroneRegistrationResponse();
    }

    @Test
    void registerDrone_caseInvalidRequest() {
        droneRegistrationRequest.setState(null);
        droneRegistrationRequest.setSerialNumber("123");
        when(registrationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.registerDrone(droneRegistrationRequest);

        verify(registrationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
        verify(registrationGateway, times(0)).saveDroneRecord(any());
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void registerDrone_caseAlreadyRegistered() {
        droneRegistrationRequest.setState(null);
        when(registrationGateway.droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber())).thenReturn(true);
        when(registrationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.registerDrone(droneRegistrationRequest);

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_ALREADY_REGISTERED);
        assertEmptyDroneRegistrationResponse(response);
    }


    @Test
    void registerDrone_caseValidRequest() {
        droneRegistrationRequest.setState(null);
        DroneRegistrationGatewayRequest expectedGatewayRequest = droneMapper.registrationRequestToGatewayRequest(droneRegistrationRequest);
        expectedGatewayRequest.setState(DroneEntity.State.IDLE.name());
        DroneRegistrationResponse expectedResponse = droneMapper.registrationRequestToRegistrationResponse(droneRegistrationRequest);
        expectedResponse.setState(DroneEntity.State.IDLE.name());
        when(registrationGateway.droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber())).thenReturn(false);
        when(registrationOutputBoundary.presentSuccessResponse(any())).thenReturn(expectedResponse);

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.registerDrone(droneRegistrationRequest);

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationGateway, times(1)).saveDroneRecord(any());
        verifySaveDroneRecordArgument(expectedGatewayRequest);
        verifyPresentSuccessResponseArgument(expectedResponse);
        assertDroneRegistrationResponseFields(expectedResponse, response);
    }


    @Test
    void updateRegisteredDrone_caseInvalidRequest() {
        droneRegistrationRequest.setSerialNumber("123");
        when(registrationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.updateRegisteredDrone(droneRegistrationRequest);

        verify(registrationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
        verify(registrationGateway, times(0)).saveDroneRecord(any());
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void updateRegisteredDrone_caseDoesNotExist() {
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(false);
        when(registrationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.updateRegisteredDrone(droneRegistrationRequest);

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void updateRegisteredDrone_caseExists() {
        droneRegistrationRequest.setState(DroneEntity.State.DELIVERED.name());
        DroneRegistrationGatewayRequest expectedGatewayRequest = droneMapper.registrationRequestToGatewayRequest(droneRegistrationRequest);
        DroneRegistrationResponse expectedResponse = droneMapper.registrationRequestToRegistrationResponse(droneRegistrationRequest);
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(registrationOutputBoundary.presentSuccessResponse(any())).thenReturn(droneRegistrationResponse);

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.updateRegisteredDrone(droneRegistrationRequest);

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationGateway, times(1)).saveDroneRecord(any());
        verifySaveDroneRecordArgument(expectedGatewayRequest);
        verifyPresentSuccessResponseArgument(expectedResponse);
        assertDroneRegistrationResponseFields(response, droneRegistrationResponse);
    }


    @Test
    void getDroneBySerialNumber_caseInvalidRequest() {
        String droneSerialNumber = "123";
        when(registrationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.getDroneBySerialNumber(droneSerialNumber);

        verify(registrationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void getDroneBySerialNumber_caseDoesNotExist() {
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(false);
        when(registrationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary
                .getDroneBySerialNumber(droneRegistrationRequest.getSerialNumber());

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void getDroneBySerialNumber_caseExist() {
        DroneRegistrationGatewayResponse expectedResponse = droneMapper.registrationResponseToGatewayRegistrationResponse(droneRegistrationResponse);
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(registrationGateway.getDroneRecordBySerialNumber(anyString())).thenReturn(expectedResponse);
        when(registrationOutputBoundary.presentSuccessResponse(any())).thenReturn(droneRegistrationResponse);

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary
                .getDroneBySerialNumber(droneRegistrationRequest.getSerialNumber());

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationGateway, times(1)).getDroneRecordBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verifyPresentSuccessResponseArgument(droneRegistrationResponse);
        assertDroneRegistrationResponseFields(droneRegistrationResponse, response);
    }


    @Test
    void deleteDroneBySerialNumber_caseInvalidRequest() {
        String invalidDroneSerialNumber = "123";
        when(registrationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary.deleteDroneBySerialNumber(invalidDroneSerialNumber);

        verify(registrationGateway, times(0)).droneExistsBySerialNumber(invalidDroneSerialNumber);
        verify(registrationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void deleteDroneBySerialNumber_caseDoesNotExist() {
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(false);
        when(registrationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary
                .deleteDroneBySerialNumber(droneRegistrationRequest.getSerialNumber());

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
        assertEmptyDroneRegistrationResponse(response);
    }

    @Test
    void deleteDroneBySerialNumber_caseExists() {
        when(registrationGateway.droneExistsBySerialNumber(anyString())).thenReturn(true);
        when(registrationOutputBoundary.presentSuccessEmptyResponse()).thenReturn(buildEmptyDroneRegistrationResponse());

        DroneRegistrationResponse response = (DroneRegistrationResponse) registrationInputBoundary
                .deleteDroneBySerialNumber(droneRegistrationRequest.getSerialNumber());

        verify(registrationGateway, times(1)).droneExistsBySerialNumber(droneRegistrationRequest.getSerialNumber());
        verify(registrationGateway, times(1)).deleteDroneRecordBySerialNumber(droneRegistrationRequest.getSerialNumber());
        assertEmptyDroneRegistrationResponse(response);
    }

}
