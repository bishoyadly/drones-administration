package com.musalasoft.dronesadministration.medication.usecases;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationUseCaseErrorMessages {

    public static final String MEDICATION_ALREADY_EXISTS = "Medication With Exact Code Already Exists";
    public static final String MEDICATION_DOES_NOT_EXISTS = "Medication With That Code Does Not Exist";
}
