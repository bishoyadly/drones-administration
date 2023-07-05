package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationLoadingErrorMessages {
    public static final String MEDICATION_LOAD_EXCEEDS_LOAD_LIMIT = "Medication Load Exceeds Drone Load Weight Limit";
    public static final String DRONE_BATTERY_CAPACITY_IS_LOW = "Drone Battery Capacity Below 25%";
    public static final String MEDICATION_ALREADY_LOADED = "Medication With That Code Already Loaded";
}
