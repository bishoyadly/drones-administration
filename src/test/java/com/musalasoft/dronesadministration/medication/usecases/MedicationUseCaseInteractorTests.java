package com.musalasoft.dronesadministration.medication.usecases;

import com.musalasoft.dronesadministration.medication.entities.MedicationEntity;
import com.musalasoft.dronesadministration.medication.entities.MedicationErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationUseCaseInteractorTests {

    @Mock
    MedicationGateway medicationGateway;

    @Mock
    MedicationOutputBoundary medicationOutputBoundary;

    MedicationInputBoundary medicationUseCaseInteractor;

    MedicationRequest medicationRequest;

    MedicationEntity medicationEntity;

    MedicationMapper medicationMapper;

    MedicationRequest buildMedicationRequest() {
        MedicationRequest request = new MedicationRequest();
        request.setCode("ABC_123");
        request.setName("Medicine-123_1mg");
        request.setWeightInGram(10);
        request.setImageUrl("https://test-image-url.com");
        return request;
    }

    MedicationGatewayResponse buildMedicationGatewayResponse() {
        MedicationGatewayResponse request = new MedicationGatewayResponse();
        request.setCode("ABC_123");
        request.setName("Medicine-123_1mg");
        request.setWeightInGram(10);
        request.setImageUrl("https://test-image-url.com");
        return request;
    }

    MedicationResponse buildNullMedicationResponse() {
        MedicationResponse response = new MedicationResponse();
        response.setCode("");
        response.setName("");
        response.setWeightInGram(0);
        response.setImageUrl("");
        return response;
    }

    MedicationEntity buildMedication() {
        MedicationEntity request = new MedicationEntity();
        request.setCode("ABC_123");
        request.setName("Medicine-123_1mg");
        request.setWeightInGram(10);
        request.setImageUrl("https://test-image-url.com");
        return request;
    }

    void assertNullMedicationResponse(MedicationResponse response) {
        assertEquals("", response.getCode());
        assertEquals("", response.getName());
        assertEquals(0, response.getWeightInGram());
        assertEquals("", response.getImageUrl());
    }

    void assertMedicationResponseFields(MedicationResponse expectedResponse, MedicationResponse actualResponse) {
        assertEquals(expectedResponse.getCode(), actualResponse.getCode());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getWeightInGram(), actualResponse.getWeightInGram());
        assertEquals(expectedResponse.getImageUrl(), actualResponse.getImageUrl());
    }

    void assertMedicationGatewayRequestFields(MedicationGatewayRequest expected, MedicationGatewayRequest actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWeightInGram(), actual.getWeightInGram());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    void verifyPresentSuccessResponseArgument(MedicationResponse expectedResponse) {
        ArgumentCaptor<MedicationResponse> argumentCaptor = ArgumentCaptor.forClass(MedicationResponse.class);
        verify(medicationOutputBoundary, times(1)).presentMedicationSuccessResponse(argumentCaptor.capture());
        assertMedicationResponseFields(expectedResponse, argumentCaptor.getValue());
    }

    void verifySaveMedicationRecordArgument(MedicationGatewayRequest expectedRequest) {
        ArgumentCaptor<MedicationGatewayRequest> argumentCaptor = ArgumentCaptor.forClass(MedicationGatewayRequest.class);
        verify(medicationGateway, times(1)).saveMedicationRecord(argumentCaptor.capture());
        assertMedicationGatewayRequestFields(expectedRequest, argumentCaptor.getValue());
    }

    @BeforeEach
    void setUp() {
        medicationMapper = new MedicationMapperImpl();
        medicationUseCaseInteractor = new MedicationUseCaseInteractor(medicationGateway, medicationOutputBoundary, medicationMapper);
        medicationRequest = buildMedicationRequest();
        medicationEntity = buildMedication();
    }

    @Test
    void addMedication_caseInvalidRequest() {
        medicationRequest.setCode("$%@");
        when(medicationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.addMedication(medicationRequest);

        verify(medicationGateway, times(0)).medicationExistsByCode(medicationRequest.getCode());
        verify(medicationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationErrorMessage.INVALID_CODE);
        assertNullMedicationResponse(response);
    }

    @Test
    void addMedication_caseAlreadyExists() {
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(true);
        when(medicationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.addMedication(medicationRequest);

        verify(medicationGateway, times(1)).medicationExistsByCode(medicationRequest.getCode());
        verify(medicationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_ALREADY_EXISTS);
        assertNullMedicationResponse(response);
    }

    @Test
    void addMedication_caseDoesNotExist() {
        MedicationResponse medicationResponse = medicationMapper.medicationRequestToMedicationResponse(medicationRequest);
        MedicationGatewayRequest gatewayRequest = medicationMapper.medicationRequestToMedicationGatewayRequest(medicationRequest);
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(false);
        when(medicationOutputBoundary.presentMedicationSuccessResponse(any())).thenReturn(medicationResponse);

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.addMedication(medicationRequest);

        verify(medicationGateway, times(1)).medicationExistsByCode(medicationRequest.getCode());
        verify(medicationOutputBoundary, times(0)).presentBadRequestFailureResponse(any());
        verifySaveMedicationRecordArgument(gatewayRequest);
        verifyPresentSuccessResponseArgument(medicationResponse);
        assertMedicationResponseFields(medicationResponse, response);
    }

    @Test
    void updateMedication_caseInvalidRequest() {
        medicationRequest.setCode("$%@");
        when(medicationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.updateMedication(medicationRequest);

        verify(medicationOutputBoundary, times(1))
                .presentBadRequestFailureResponse(MedicationErrorMessage.INVALID_CODE);
        verify(medicationGateway, times(0)).medicationExistsByCode(medicationRequest.getCode());
        assertNullMedicationResponse(response);
    }

    @Test
    void updateMedication_caseDoesNotExist() {
        when(medicationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildNullMedicationResponse());
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(false);

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.updateMedication(medicationRequest);

        verify(medicationGateway, times(1)).medicationExistsByCode(medicationRequest.getCode());
        verify(medicationGateway, times(0)).saveMedicationRecord(any());
        verify(medicationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        assertNullMedicationResponse(response);
    }

    @Test
    void updateMedication_caseValidRequest() {
        MedicationGatewayRequest gatewayRequest = medicationMapper.medicationRequestToMedicationGatewayRequest(medicationRequest);
        MedicationResponse expectedMedicationResponse = medicationMapper.medicationRequestToMedicationResponse(medicationRequest);
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(true);
        when(medicationOutputBoundary.presentMedicationSuccessResponse(any())).thenReturn(expectedMedicationResponse);

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.updateMedication(medicationRequest);
        verifySaveMedicationRecordArgument(gatewayRequest);
        verifyPresentSuccessResponseArgument(expectedMedicationResponse);
        assertMedicationResponseFields(expectedMedicationResponse, response);
    }


    @Test
    void getMedicationByCode_caseNotValidCode() {
        when(medicationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildNullMedicationResponse());
        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.getMedicationByCode("a$%#");
        verify(medicationOutputBoundary, times(1)).presentBadRequestFailureResponse(MedicationErrorMessage.INVALID_CODE);
        assertNullMedicationResponse(response);
    }

    @Test
    void getMedicationByCode_caseDoesNotExist() {
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(false);
        when(medicationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.getMedicationByCode("ABC_123");

        verify(medicationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        assertNullMedicationResponse(response);
    }

    @Test
    void getMedicationByCode_caseMedicationExist() {
        String medicationCode = "ABC_123";
        MedicationGatewayResponse expectedGatewayResponse = buildMedicationGatewayResponse();
        MedicationResponse expectedResponse = medicationMapper.
                medicationGatewayResponseToMedicationResponse(expectedGatewayResponse);
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(true);
        when(medicationGateway.getMedicationRecordByCode(medicationCode)).thenReturn(expectedGatewayResponse);
        when(medicationOutputBoundary.presentMedicationSuccessResponse(any())).thenReturn(expectedResponse);

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.getMedicationByCode(medicationCode);

        verifyPresentSuccessResponseArgument(expectedResponse);
        assertMedicationResponseFields(expectedResponse, response);
    }


    @Test
    void deleteMedicationByCode_caseNotValidCode() {
        when(medicationOutputBoundary.presentBadRequestFailureResponse(any())).thenReturn(buildNullMedicationResponse());
        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.deleteMedicationByCode("a$%#");
        verify(medicationOutputBoundary, times(1)).presentBadRequestFailureResponse(MedicationErrorMessage.INVALID_CODE);
        assertNullMedicationResponse(response);
    }

    @Test
    void deleteMedicationByCode_caseDoesNotExist() {
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(false);
        when(medicationOutputBoundary.presentNotFoundFailureResponse(any())).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.deleteMedicationByCode("ABC_123");

        verify(medicationOutputBoundary, times(1))
                .presentNotFoundFailureResponse(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        assertNullMedicationResponse(response);
    }

    @Test
    void deleteMedicationByCode_caseMedicationExist() {
        String medicationCode = "ABC_123";
        when(medicationGateway.medicationExistsByCode(any())).thenReturn(true);
        when(medicationOutputBoundary.presentMedicationSuccessEmptyResponse()).thenReturn(buildNullMedicationResponse());

        MedicationResponse response = (MedicationResponse) medicationUseCaseInteractor.deleteMedicationByCode(medicationCode);

        verify(medicationGateway, times(1)).deleteMedicationRecordByCode(medicationCode);
        assertNullMedicationResponse(response);
    }
}
