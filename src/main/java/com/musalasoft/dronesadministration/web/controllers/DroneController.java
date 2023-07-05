package com.musalasoft.dronesadministration.web.controllers;

import com.musalasoft.dronesadministration.controller.DroneApi;
import com.musalasoft.dronesadministration.drone.adapters.DroneComponent;
import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import com.musalasoft.dronesadministration.model.DroneModelDto;
import com.musalasoft.dronesadministration.model.DronePageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DroneController implements DroneApi {

    private final DroneComponent droneComponent;
    private final ControllerMapper controllerMapper;

    @Autowired
    public DroneController(DroneComponent droneComponent, ControllerMapper controllerMapper) {
        this.droneComponent = droneComponent;
        this.controllerMapper = controllerMapper;
    }

    @Override
    public ResponseEntity<Object> registerDrone(DroneModelDto requestDto) {
        DroneRegistrationRequest registrationRequest = controllerMapper.droneRegistrationRequestToDroneModelDto(requestDto);
        try {
            DroneRegistrationResponse registrationResponse = droneComponent.registerDrone(registrationRequest);
            DroneModelDto responseDto = controllerMapper.droneRegistrationResponseToDroneModelDto(registrationResponse);
            return ResponseEntity.ok(responseDto);
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> updateRegisteredDrone(String droneSerialNumber, DroneModelDto requestDto) {
        DroneRegistrationRequest registrationRequest = controllerMapper.droneRegistrationRequestToDroneModelDto(requestDto);
        registrationRequest.setSerialNumber(droneSerialNumber);
        try {
            DroneRegistrationResponse registrationResponse = droneComponent.updateRegisteredDrone(registrationRequest);
            return ResponseEntity.ok(controllerMapper.droneRegistrationResponseToDroneModelDto(registrationResponse));
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> getDroneBySerialNumber(String droneSerialNumber) {
        try {
            DroneRegistrationResponse registrationResponse = droneComponent.getDroneBySerialNumber(droneSerialNumber);
            DroneModelDto responseDto = controllerMapper.droneRegistrationResponseToDroneModelDto(registrationResponse);
            return ResponseEntity.ok(responseDto);
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> deleteDroneBySerialNumber(String droneSerialNumber) {
        try {
            droneComponent.deleteDroneBySerialNumber(droneSerialNumber);
            return ResponseEntity.noContent().build();
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> loadDroneWithMedications(String droneSerialNumber, List<String> medicationCodesList) {
        MedicationLoadingRequest request = new MedicationLoadingRequest();
        request.setDroneSerialNumber(droneSerialNumber);
        request.setMedicationCodesList(medicationCodesList);
        try {
            MedicationLoadingResponse response = droneComponent.loadDroneWithMedications(request);
            DroneModelDto modelDto = controllerMapper.medicationLoadingResponseToDroneModelDto(response);
            return ResponseEntity.ok(modelDto);
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> getDronesAvailableForLoading(Integer pageNumber, Integer pageSize) {
        MedicationLoadingRequest request = new MedicationLoadingRequest();
        request.setDronesPageNumber(pageNumber);
        request.setDronesPageSize(pageSize);
        try {
            MedicationLoadingResponsePage responsePage = droneComponent.getDronesAvailableForLoading(request);
            DronePageDto dronePageDto = controllerMapper.medicationLoadingResponsePageToDroneModelPage(responsePage);
            return ResponseEntity.ok(dronePageDto);
        } catch (DroneComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }
}
