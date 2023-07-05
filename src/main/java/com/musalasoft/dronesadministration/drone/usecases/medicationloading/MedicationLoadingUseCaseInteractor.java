package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneNotFoundException;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationNotFoundException;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationUseCaseErrorMessages;

import java.util.LinkedList;
import java.util.List;

class MedicationLoadingUseCaseInteractor implements MedicationLoadingInputBoundary {

    private final MedicationLoadingGateway gateway;
    private final MedicationLoadingOutputBoundary outputBoundary;
    private final MedicationLoadingMapper medicationLoadingMapper;

    public MedicationLoadingUseCaseInteractor(MedicationLoadingGateway gateway,
                                              MedicationLoadingOutputBoundary outputBoundary,
                                              MedicationLoadingMapper medicationLoadingMapper) {
        this.gateway = gateway;
        this.outputBoundary = outputBoundary;
        this.medicationLoadingMapper = medicationLoadingMapper;
    }

    @Override
    public Object loadDroneWithMedications(MedicationLoadingRequest request) {
        try {
            return processLoadDroneWithMedicationsRequest(request);
        } catch (DroneNotFoundException | MedicationNotFoundException exception) {
            return presentNotFoundFailureResponse(exception.getMessage());
        } catch (MedicationLoadingException exception) {
            return presentBadRequestResponse(exception.getMessage());
        }
    }

    @Override
    public Object getDronesAvailableForLoading(MedicationLoadingRequest request) {
        MedicationLoadingGatewayRequest gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        MedicationLoadingGatewayResponsePage gatewayResponsePage = gateway.getDronesPaginated(gatewayRequest);
        MedicationLoadingResponsePage responsePage = medicationLoadingMapper.gatewayResponsePageToResponsePage(gatewayResponsePage);
        List<MedicationLoadingResponse> availableForLoadingResponseList = filterDronesAvailableForLoading(responsePage.getMedicationLoadingResponseList());
        responsePage.setMedicationLoadingResponseList(availableForLoadingResponseList);
        return presentSuccessResponseList(responsePage);
    }

    @Override
    public Object getDronesPage(MedicationLoadingRequest request) {
        MedicationLoadingGatewayRequest gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        MedicationLoadingGatewayResponsePage gatewayResponsePage = gateway.getDronesPaginated(gatewayRequest);
        MedicationLoadingResponsePage responsePage = medicationLoadingMapper.gatewayResponsePageToResponsePage(gatewayResponsePage);
        return presentSuccessResponseList(responsePage);
    }

    private List<MedicationLoadingResponse> filterDronesAvailableForLoading(List<MedicationLoadingResponse> responseList) {
        List<MedicationLoadingResponse> filteredList = new LinkedList<>();
        for (MedicationLoadingResponse response : responseList) {
            Integer currentDroneLoad = 0;
            for (MedicationResponse medicationResponse : response.getMedicationResponseList()) {
                currentDroneLoad += medicationResponse.getWeightInGram();
            }
            boolean batteryCapacityMoreThan25Percent = response.getDroneBatteryCapacityInPercentage() >= 25;
            boolean droneLoadLimitMoreThanCurrentDroneLoad = response.getDroneLoadWeightLimitInGram() > currentDroneLoad;
            if (batteryCapacityMoreThan25Percent && droneLoadLimitMoreThanCurrentDroneLoad)
                filteredList.add(response);
        }
        return filteredList;
    }

    private Object processLoadDroneWithMedicationsRequest(MedicationLoadingRequest request) {
        boolean droneDoesNotExist = !doesDroneExistBySerialNumber(request.getDroneSerialNumber());
        boolean medicationsDoNotExist = !doMedicationsExistByCodesList(request.getMedicationCodesList());
        if (droneDoesNotExist)
            throw new DroneNotFoundException(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
        if (medicationsDoNotExist)
            throw new MedicationNotFoundException(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS);
        MedicationLoadingGatewayRequest gatewayRequest = medicationLoadingMapper.requestToGatewayRequest(request);
        gatewayRequest.setDroneState(DroneEntity.State.LOADING.name());
        validateDroneForMedicationLoading(gatewayRequest);
        MedicationLoadingGatewayResponse gatewayResponse = updateDroneRecordWithMedications(gatewayRequest);
        MedicationLoadingResponse response = medicationLoadingMapper.gatewayResponseToResponse(gatewayResponse);
        return presentSuccessResponse(response);
    }

    private void validateDroneForMedicationLoading(MedicationLoadingGatewayRequest gatewayRequest) {
        MedicationLoadingGatewayResponse gatewayResponse = getDroneRecordBySerialNumber(gatewayRequest.getDroneSerialNumber());
        if (medicationAlreadyLoaded(gatewayResponse.getMedicationGatewayResponseList(), gatewayRequest.getMedicationCodesList()))
            throw new MedicationLoadingException(MedicationLoadingErrorMessages.MEDICATION_ALREADY_LOADED);
        if (gatewayResponse.getDroneBatteryCapacityInPercentage() < 25)
            throw new MedicationLoadingException(MedicationLoadingErrorMessages.DRONE_BATTERY_CAPACITY_IS_LOW);
        List<MedicationGatewayResponse> medicationGatewayResponseList = getMedicationRecordsByCodesList(gatewayRequest.getMedicationCodesList());
        Integer currentDroneLoadInGram = getCurrentDroneLoadInGram(gatewayResponse.getMedicationGatewayResponseList());
        Integer newMedicationsLoadInGram = getNewMedicationsLoadInGram(medicationGatewayResponseList);
        int resultDroneLoadInGram = currentDroneLoadInGram + newMedicationsLoadInGram;
        if (resultDroneLoadInGram > gatewayResponse.getDroneLoadWeightLimitInGram())
            throw new MedicationLoadingException(MedicationLoadingErrorMessages.MEDICATION_LOAD_EXCEEDS_LOAD_LIMIT);
    }

    private boolean medicationAlreadyLoaded(List<MedicationGatewayResponse> responseList,
                                            List<String> requestMedicationCodesList) {
        for (String medicationCode : requestMedicationCodesList) {
            for (MedicationGatewayResponse response : responseList) {
                if (response.getCode().equals(medicationCode))
                    return true;
            }
        }
        return false;
    }

    private Integer getNewMedicationsLoadInGram(List<MedicationGatewayResponse> medicationGatewayResponseList) {
        Integer newMedicationsLoadInGram = 0;
        for (MedicationGatewayResponse medicationGatewayResponse : medicationGatewayResponseList) {
            newMedicationsLoadInGram += medicationGatewayResponse.getWeightInGram();
        }
        return newMedicationsLoadInGram;
    }

    private Integer getCurrentDroneLoadInGram(List<MedicationGatewayResponse> medicationGatewayResponseList) {
        Integer currentDroneLoadInGram = 0;
        for (MedicationGatewayResponse medicationGatewayResponse : medicationGatewayResponseList) {
            currentDroneLoadInGram += medicationGatewayResponse.getWeightInGram();
        }
        return currentDroneLoadInGram;
    }

    //#region Gateway Methods

    private boolean doesDroneExistBySerialNumber(String serialNumber) {
        return gateway.droneExistsBySerialNumber(serialNumber);
    }

    private boolean doMedicationsExistByCodesList(List<String> medicationCodesList) {
        return gateway.doMedicationsExistByCodesList(medicationCodesList);
    }

    private MedicationLoadingGatewayResponse getDroneRecordBySerialNumber(String serialNumber) {
        return gateway.getDroneRecordBySerialNumber(serialNumber);
    }

    private List<MedicationGatewayResponse> getMedicationRecordsByCodesList(List<String> medicationCodesList) {
        return gateway.getMedicationRecordsByCodesList(medicationCodesList);
    }

    private MedicationLoadingGatewayResponse updateDroneRecordWithMedications(MedicationLoadingGatewayRequest gatewayRequest) {
        return gateway.updateDroneRecordWithMedications(gatewayRequest);
    }

    //#endregion

    //#region Presenter Methods

    private Object presentSuccessResponse(MedicationLoadingResponse response) {
        return outputBoundary.presentSuccessResponse(response);
    }

    private Object presentSuccessResponseList(MedicationLoadingResponsePage responseList) {
        return outputBoundary.presentSuccessResponseList(responseList);
    }

    private Object presentNotFoundFailureResponse(String errorMessage) {
        return outputBoundary.presentNotFoundFailureResponse(errorMessage);
    }

    private Object presentBadRequestResponse(String errorMessage) {
        return outputBoundary.presentBadRequestFailureResponse(errorMessage);
    }

    //#endregion
}
