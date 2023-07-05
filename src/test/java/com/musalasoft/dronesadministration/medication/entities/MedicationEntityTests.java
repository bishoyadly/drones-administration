package com.musalasoft.dronesadministration.medication.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicationEntityTests {

    private MedicationEntity medicationEntity;

    private void assertMedicationValidationErrorMessage(MedicationEntity medicationEntity, String expectedErrorMessage) {
        String actualErrorMessage = "";
        try {
            medicationEntity.validateMedicationFields();
        } catch (MedicationException exception) {
            actualErrorMessage = exception.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    MedicationEntity buildMedication() {
        MedicationEntity medicationEntity = new MedicationEntity();
        medicationEntity.setCode("ABC_123");
        medicationEntity.setName("Medicine-123_1mg");
        medicationEntity.setWeightInGram(10);
        medicationEntity.setImageUrl("https://test-image-url.com");
        return medicationEntity;
    }

    @BeforeEach
    void setUp() {
        medicationEntity = buildMedication();
    }

    @Test
    void validateName_caseNullValue() {
        medicationEntity.setName(null);
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_NAME);
    }

    @Test
    void validateName_caseContainsInvalidCharacters() {
        medicationEntity.setName("Medicine-123_1mg$@#");
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_NAME);
    }

    @Test
    void validateName_caseContainsOnlyValidCharacters() {
        medicationEntity.setName("Medicine-123_1mg");
        assertDoesNotThrow(medicationEntity::validateMedicationFields);
    }

    @Test
    void validateWeight_caseNullValue() {
        medicationEntity.setWeightInGram(null);
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_WEIGHT);
    }

    @Test
    void validateWeight_caseInvalidValue() {
        medicationEntity.setWeightInGram(0);
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_WEIGHT);

        medicationEntity.setWeightInGram(-1);
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_WEIGHT);
    }

    @Test
    void validateWeight_caseValidValue() {
        medicationEntity.setWeightInGram(10);
        assertDoesNotThrow(medicationEntity::validateMedicationFields);
    }

    @Test
    void validateCode_caseNull() {
        medicationEntity.setCode(null);
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_CODE);
    }

    @Test
    void validateCode_caseContainsLowerCaseCharacters() {
        medicationEntity.setCode("ABcd_123");
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_CODE);
    }

    @Test
    void validateCode_caseContainsInvalidCharacters() {
        medicationEntity.setCode("ABC_123$#@");
        assertThrows(MedicationException.class, medicationEntity::validateMedicationFields);
        assertMedicationValidationErrorMessage(medicationEntity, MedicationErrorMessage.INVALID_CODE);
    }

    @Test
    void validateCode_caseContainsOnlyValidCharacters() {
        medicationEntity.setCode("ABCXYZ_12345");
        assertDoesNotThrow(medicationEntity::validateMedicationFields);
    }
}
