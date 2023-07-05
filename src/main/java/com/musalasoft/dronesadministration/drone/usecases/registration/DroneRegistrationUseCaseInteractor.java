package com.musalasoft.dronesadministration.drone.usecases.registration;

import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.entities.DroneEntityException;

class DroneRegistrationUseCaseInteractor implements DroneRegistrationInputBoundary {

    private final DroneRegistrationGateway registrationGateway;
    private final DroneRegistrationOutputBoundary registrationOutputBoundary;
    private final DroneMapper droneMapper;

    public DroneRegistrationUseCaseInteractor(DroneRegistrationGateway registrationGateway,
                                              DroneRegistrationOutputBoundary registrationOutputBoundary, DroneMapper droneMapper) {
        this.registrationGateway = registrationGateway;
        this.registrationOutputBoundary = registrationOutputBoundary;
        this.droneMapper = droneMapper;
    }

    @Override
    public Object registerDrone(DroneRegistrationRequest request) {
        try {
            return processDroneRegistrationRequest(request);
        } catch (DroneEntityException | DroneBadRequestException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object updateRegisteredDrone(DroneRegistrationRequest request) {
        try {
            return processUpdateRegisteredDroneRequest(request);
        } catch (DroneEntityException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (DroneNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object getDroneBySerialNumber(String serialNumber) {
        try {
            return processGetDroneBySerialNumberRequest(serialNumber);
        } catch (DroneEntityException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (DroneNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    @Override
    public Object deleteDroneBySerialNumber(String serialNumber) {
        try {
            return processDeleteDroneBySerialNumberRequest(serialNumber);
        } catch (DroneEntityException exception) {
            return presentBadRequestFailureResponse(exception.getMessage());
        } catch (DroneNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        }
    }

    private Object processDroneRegistrationRequest(DroneRegistrationRequest request) {
        DroneEntity droneEntity = droneMapper.registrationRequestToDroneEntity(request);
        droneEntity.validateDroneFields();
        if (doesDroneExistsBySerialNumber(request.getSerialNumber()))
            throw new DroneBadRequestException(DroneRegistrationUseCaseErrorMessages.DRONE_ALREADY_REGISTERED);
        else {
            DroneRegistrationGatewayRequest gatewayRequest = droneMapper.droneEntityToRegistrationGatewayRequest(droneEntity);
            saveDroneRecord(gatewayRequest);
            DroneRegistrationResponse response = droneMapper.droneEntityToRegistrationResponse(droneEntity);
            return presentSuccessResponse(response);
        }
    }

    private Object processUpdateRegisteredDroneRequest(DroneRegistrationRequest request) {
        DroneEntity droneEntity = droneMapper.registrationRequestToDroneEntity(request);
        droneEntity.validateDroneFields();
        if (doesDroneExistsBySerialNumber(request.getSerialNumber())) {
            DroneRegistrationGatewayRequest gatewayRequest = droneMapper.droneEntityToRegistrationGatewayRequest(droneEntity);
            saveDroneRecord(gatewayRequest);
            DroneRegistrationResponse response = droneMapper.droneEntityToRegistrationResponse(droneEntity);
            return presentSuccessResponse(response);
        } else
            throw new DroneNotFoundException(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }

    private Object processGetDroneBySerialNumberRequest(String serialNumber) {
        DroneEntity.validateSerialNumberFormat(serialNumber);
        if (doesDroneExistsBySerialNumber(serialNumber)) {
            DroneRegistrationGatewayResponse gatewayResponse = getDroneRecordBySerialNumber(serialNumber);
            DroneRegistrationResponse registrationResponse = droneMapper.registrationGatewayResponseToRegistrationResponse(gatewayResponse);
            return presentSuccessResponse(registrationResponse);
        } else
            throw new DroneNotFoundException(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }

    private Object processDeleteDroneBySerialNumberRequest(String serialNumber) {
        DroneEntity.validateSerialNumberFormat(serialNumber);
        if (doesDroneExistsBySerialNumber(serialNumber)) {
            deleteDroneRecordBySerialNumber(serialNumber);
            return presentSuccessEmptyResponse();
        } else
            throw new DroneNotFoundException(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }

    //#region Gateway Methods

    boolean doesDroneExistsBySerialNumber(String serialNumber) {
        return registrationGateway.droneExistsBySerialNumber(serialNumber);
    }

    void saveDroneRecord(DroneRegistrationGatewayRequest gatewayRequest) {
        registrationGateway.saveDroneRecord(gatewayRequest);
    }

    DroneRegistrationGatewayResponse getDroneRecordBySerialNumber(String serialNumber) {
        return registrationGateway.getDroneRecordBySerialNumber(serialNumber);
    }

    void deleteDroneRecordBySerialNumber(String serialNumber) {
        registrationGateway.deleteDroneRecordBySerialNumber(serialNumber);
    }

    //#endregion

    //#region Presenter Methods

    Object presentSuccessResponse(DroneRegistrationResponse response) {
        return registrationOutputBoundary.presentSuccessResponse(response);
    }

    Object presentSuccessEmptyResponse() {
        return registrationOutputBoundary.presentSuccessEmptyResponse();
    }

    Object presentBadRequestFailureResponse(String errorMessage) {
        return registrationOutputBoundary.presentBadRequestFailureResponse(errorMessage);
    }

    Object presentNotFoundFailureResponse(String errorMessage) {
        return registrationOutputBoundary.presentNotFoundFailureResponse(errorMessage);
    }

    //#endregion
}
