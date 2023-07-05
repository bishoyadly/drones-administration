package com.musalasoft.dronesadministration.drone.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneEntityTests {

    private DroneEntity droneEntity;

    private void assertDroneValidationErrorMessage(DroneEntity droneEntity, String expectedErrorMessage) {
        String actualErrorMessage = "";
        try {
            droneEntity.validateDroneFields();
        } catch (DroneEntityException exception) {
            actualErrorMessage = exception.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    private DroneEntity buildDrone() {
        DroneEntity droneEntity = new DroneEntity();
        droneEntity.setSerialNumber(RandomStringUtils.random(10, true, true));
        droneEntity.setModel(DroneEntity.Model.HEAVY_WEIGHT);
        droneEntity.setLoadWeightLimitInGram(500);
        droneEntity.setBatteryCapacityInPercentage(100);
        return droneEntity;
    }

    @BeforeEach
    void setUp() {
        this.droneEntity = buildDrone();
    }

    @Test
    void validateSerialNumberFormat_caseNull() {
        droneEntity.setSerialNumber(null);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
    }

    @Test
    void validateSerialNumberFormat_caseMoreThanHundredCharacters() {
        String randomSerialNumber = RandomStringUtils.random(101, true, true);
        droneEntity.setSerialNumber(randomSerialNumber);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
    }

    @Test
    void validateSerialNumberFormat_caseLessThanSevenCharacters() {
        String randomSerialNumber = RandomStringUtils.random(5, true, true);
        droneEntity.setSerialNumber(randomSerialNumber);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
    }

    @Test
    void validateSerialNumberFormat_caseValidSerialNumber() {
        String randomSerialNumber = RandomStringUtils.random(7, true, true);
        droneEntity.setSerialNumber(randomSerialNumber);
        assertDoesNotThrow(droneEntity::validateDroneFields);
    }


    @Test
    void validateLoadWeightLimit_caseNull() {
        droneEntity.setLoadWeightLimitInGram(null);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_LOAD_WEIGHT_LIMIT);
    }

    @Test
    void validateLoadWeightLimit_caseMoreThanFiveHundredGrams() {
        droneEntity.setLoadWeightLimitInGram(501);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_LOAD_WEIGHT_LIMIT);
    }

    @Test
    void validateLoadWeightLimit_caseLessThanFiveHundredGrams() {
        droneEntity.setLoadWeightLimitInGram(499);
        assertDoesNotThrow(droneEntity::validateDroneFields);
    }

    @Test
    void validateLoadWeightLimit_caseEqualFiveHundredGrams() {
        droneEntity.setLoadWeightLimitInGram(500);
        assertDoesNotThrow(droneEntity::validateDroneFields);
    }


    @Test
    void validateBatteryCapacity_caseNull() {
        droneEntity.setBatteryCapacityInPercentage(null);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_BATTERY_CAPACITY);
    }

    @Test
    void validateBatteryCapacity_caseMoreThanHundredPercent() {
        droneEntity.setBatteryCapacityInPercentage(101);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_BATTERY_CAPACITY);
    }

    @Test
    void validateBatteryCapacity_caselessThanZeroPercent() {
        droneEntity.setBatteryCapacityInPercentage(-1);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_BATTERY_CAPACITY);
    }

    @Test
    void validateModel_caseNull() {
        droneEntity.setModel(null);
        assertThrows(DroneEntityException.class, droneEntity::validateDroneFields);
        assertDroneValidationErrorMessage(droneEntity, DroneErrorMessages.INVALID_MODEL);
    }

    @Test
    void validateDroneDefaultStateIsIdle() {
        assertEquals(DroneEntity.State.IDLE, droneEntity.getState());
    }
}
