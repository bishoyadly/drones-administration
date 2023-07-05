package com.musalasoft.dronesadministration.drone.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DroneErrorMessages {

    public static final String INVALID_SERIAL_NUMBER_FORMAT = "Invalid Serial Number Format";
    public static final String INVALID_LOAD_WEIGHT_LIMIT = "Invalid Load Weight Limit: Weight Limit Should Not Exceed 500 Grams";
    public static final String INVALID_BATTERY_CAPACITY = "Invalid Value For Batter Capacity";
    public static final String INVALID_MODEL = "Invalid Model";
}
