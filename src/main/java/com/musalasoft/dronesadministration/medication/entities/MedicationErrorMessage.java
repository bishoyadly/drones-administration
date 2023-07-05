package com.musalasoft.dronesadministration.medication.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationErrorMessage {
    public static final String INVALID_NAME = "Invalid Name: name contains unallowed characters";
    public static final String INVALID_WEIGHT = "Invalid Weight Value";
    public static final String INVALID_CODE = "Invalid Code: code contains unallowed characters";
}
