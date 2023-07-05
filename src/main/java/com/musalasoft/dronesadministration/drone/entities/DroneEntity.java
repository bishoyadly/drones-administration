package com.musalasoft.dronesadministration.drone.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class DroneEntity {
    private String serialNumber;
    private Integer loadWeightLimitInGram;
    private Integer batteryCapacityInPercentage;
    private Model model;
    private State state;

    public enum Model {
        LIGHT_WEIGHT, MIDDLE_WEIGHT, CRUISE_WEIGHT, HEAVY_WEIGHT
    }

    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

    public DroneEntity() {
        this.state = State.IDLE;
    }

    public void validateDroneFields() {
        validateSerialNumberFormat();
        validateLoadWeightLimit();
        validateBatterCapacity();
        validateModel();
    }

    public static void validateSerialNumberFormat(String serialNumber) {
        if (Objects.isNull(serialNumber) || serialNumber.length() > 100 || serialNumber.length() < 7)
            throw new DroneEntityException(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT);
    }

    private void validateSerialNumberFormat() {
        validateSerialNumberFormat(this.serialNumber);
    }

    private void validateLoadWeightLimit() {
        if (Objects.isNull(this.loadWeightLimitInGram) || this.loadWeightLimitInGram > 500)
            throw new DroneEntityException(DroneErrorMessages.INVALID_LOAD_WEIGHT_LIMIT);
    }

    private void validateBatterCapacity() {
        if (Objects.isNull(this.batteryCapacityInPercentage) || this.batteryCapacityInPercentage > 100 || this.batteryCapacityInPercentage < 0)
            throw new DroneEntityException(DroneErrorMessages.INVALID_BATTERY_CAPACITY);
    }

    private void validateModel() {
        if (Objects.isNull(this.model))
            throw new DroneEntityException(DroneErrorMessages.INVALID_MODEL);
    }
}
