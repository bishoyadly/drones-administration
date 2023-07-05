package com.musalasoft.dronesadministration.web.controllers;

import com.musalasoft.dronesadministration.controller.MedicationApi;
import com.musalasoft.dronesadministration.medication.adapters.MedicationComponentException;
import com.musalasoft.dronesadministration.medication.adapters.MedicationComponent;
import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.model.MedicationModelDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MedicationController implements MedicationApi {

    private final MedicationComponent medicationComponent;

    private final ControllerMapper controllerMapper;

    public MedicationController(MedicationComponent medicationComponent, ControllerMapper controllerMapper) {
        this.medicationComponent = medicationComponent;
        this.controllerMapper = controllerMapper;
    }

    @Override
    public ResponseEntity<Object> addMedication(MedicationModelDto modelDto) {
        MedicationRequest medicationRequest = controllerMapper.modelDtoToRequest(modelDto);
        try {
            medicationComponent.addMedication(medicationRequest);
            return ResponseEntity.ok(modelDto);
        } catch (MedicationComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> updateMedication(String medicationCode, MedicationModelDto modelDto) {
        MedicationRequest medicationRequest = controllerMapper.modelDtoToRequest(modelDto);
        medicationRequest.setCode(medicationCode);
        try {
            MedicationResponse response = medicationComponent.updateMedication(medicationRequest);
            return ResponseEntity.ok(controllerMapper.responseToModelDto(response));
        } catch (MedicationComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> getMedicationByCode(String medicationCode) {
        try {
            MedicationResponse response = medicationComponent.getMedicationByCode(medicationCode);
            MedicationModelDto modelDto = controllerMapper.responseToModelDto(response);
            return ResponseEntity.ok(modelDto);
        } catch (MedicationComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }

    @Override
    public ResponseEntity<Object> deleteMedicationByCode(String medicationCode) {
        try {
            medicationComponent.deleteMedicationByCode(medicationCode);
            return ResponseEntity.noContent().build();
        } catch (MedicationComponentException exception) {
            return ResponseEntity.status(exception.getProblemDto().getHttpStatusCode()).body(exception.getProblemDto());
        }
    }
}
