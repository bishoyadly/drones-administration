package com.musalasoft.dronesadministration.medication.usecases;

import com.musalasoft.dronesadministration.medication.entities.MedicationEntity;
import com.musalasoft.dronesadministration.medication.entities.MedicationException;

class MedicationUseCaseInteractor implements MedicationInputBoundary {

    private final MedicationGateway medicationGateway;
    private final MedicationOutputBoundary medicationOutputBoundary;
    private final MedicationMapper medicationMapper;

    public MedicationUseCaseInteractor(MedicationGateway medicationGateway, MedicationOutputBoundary medicationOutputBoundary,
                                       MedicationMapper medicationMapper) {
        this.medicationGateway = medicationGateway;
        this.medicationOutputBoundary = medicationOutputBoundary;
        this.medicationMapper = medicationMapper;
    }

    @Override
    public Object addMedication(MedicationRequest request) {
        try {
            return processAddMedicationRequest(request);
        } catch (MedicationException | MedicationBadRequestException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object updateMedication(MedicationRequest request) {
        try {
            return processUpdateMedicationRequest(request);
        } catch (MedicationException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (MedicationNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object getMedicationByCode(String code) {
        try {
            return processGetMedicationByCodeRequest(code);
        } catch (MedicationException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (MedicationNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object deleteMedicationByCode(String code) {
        try {
            return processDeleteMedicationByCodeRequest(code);
        } catch (MedicationException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (MedicationNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    private Object processAddMedicationRequest(MedicationRequest request) {
        MedicationEntity medicationEntity = medicationMapper.medicationRequestToMedicationEntity(request);
        medicationEntity.validateMedicationFields();
        if (doesMedicationExist(request.getCode())) {
            throw new MedicationBadRequestException(MedicationUseCaseErrorMessages.MEDICATION_ALREADY_EXISTS);
        } else {
            MedicationGatewayRequest gatewayRequest = medicationMapper.medicationRequestToMedicationGatewayRequest(request);
            saveMedication(gatewayRequest);
            MedicationResponse response = medicationMapper.medicationRequestToMedicationResponse(request);
            return presentMedicationSuccessResponse(response);
        }
    }

    private Object processUpdateMedicationRequest(MedicationRequest request) {
        MedicationEntity medicationEntity = medicationMapper.medicationRequestToMedicationEntity(request);
        medicationEntity.validateMedicationFields();
        if (doesMedicationExist(request.getCode())) {
            MedicationGatewayRequest gatewayRequest = medicationMapper.medicationRequestToMedicationGatewayRequest(request);
            saveMedication(gatewayRequest);
            MedicationResponse response = medicationMapper.medicationRequestToMedicationResponse(request);
            return presentMedicationSuccessResponse(response);
        } else {
            throw new MedicationNotFoundException(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        }
    }

    private Object processGetMedicationByCodeRequest(String code) {
        MedicationEntity.validateCode(code);
        if (doesMedicationExist(code)) {
            MedicationGatewayResponse medicationGatewayResponse = getMedicationRecordByCode(code);
            MedicationResponse medicationResponse = medicationMapper.medicationGatewayResponseToMedicationResponse(medicationGatewayResponse);
            return presentMedicationSuccessResponse(medicationResponse);
        } else {
            throw new MedicationNotFoundException(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        }
    }

    private Object processDeleteMedicationByCodeRequest(String code) {
        MedicationEntity.validateCode(code);
        if (doesMedicationExist(code)) {
            deleteMedicationRecordByCode(code);
            return presentMedicationSuccessEmptyResponse();
        } else {
            throw new MedicationNotFoundException(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        }
    }

    //#region Gateway Methods

    private void saveMedication(MedicationGatewayRequest gatewayRequest) {
        medicationGateway.saveMedicationRecord(gatewayRequest);
    }

    private boolean doesMedicationExist(String code) {
        return medicationGateway.medicationExistsByCode(code);
    }

    private MedicationGatewayResponse getMedicationRecordByCode(String code) {
        return medicationGateway.getMedicationRecordByCode(code);
    }

    private void deleteMedicationRecordByCode(String code) {
        medicationGateway.deleteMedicationRecordByCode(code);
    }

    //#endregion

    //#region Presenter Methods

    private Object presentBadRequestFailureResponse(String errorMessage) {
        return medicationOutputBoundary.presentBadRequestFailureResponse(errorMessage);
    }

    private Object presentNotFoundFailureResponse(String errorMessage) {
        return medicationOutputBoundary.presentNotFoundFailureResponse(errorMessage);
    }

    private Object presentMedicationSuccessResponse(MedicationResponse response) {
        return medicationOutputBoundary.presentMedicationSuccessResponse(response);
    }

    private Object presentMedicationSuccessEmptyResponse() {
        return medicationOutputBoundary.presentMedicationSuccessEmptyResponse();
    }

    //#endregion
}
