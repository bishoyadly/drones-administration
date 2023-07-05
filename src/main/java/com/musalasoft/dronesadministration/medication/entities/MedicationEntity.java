package com.musalasoft.dronesadministration.medication.entities;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
public class MedicationEntity {

    private String code;
    private String name;
    private Integer weightInGram;
    private String imageUrl;

    public void validateMedicationFields() {
        validateName();
        validateWeight();
        validateCode();
    }

    public static void validateCode(String code) {
        if (Objects.isNull(code))
            throw new MedicationException(MedicationErrorMessage.INVALID_CODE);
        for (int i = 0; i < code.length(); i++) {
            Character character = code.charAt(i);
            boolean isNotAllowedCharacter = !isAllowedCharacterInCode(character);
            if (isNotAllowedCharacter)
                throw new MedicationException(MedicationErrorMessage.INVALID_CODE);
        }
    }

    private void validateName() {
        if (Objects.isNull(this.name))
            throw new MedicationException(MedicationErrorMessage.INVALID_NAME);
        for (int i = 0; i < this.name.length(); i++) {
            Character character = name.charAt(i);
            boolean isNotAllowedCharacter = !isAllowedCharacterInName(character);
            if (isNotAllowedCharacter)
                throw new MedicationException(MedicationErrorMessage.INVALID_NAME);
        }
    }

    private boolean isAllowedCharacterInName(Character character) {
        return StringUtils.isAlphanumeric(character.toString()) || character == '-' || character == '_';
    }

    private void validateWeight() {
        if (Objects.isNull(this.weightInGram) || this.weightInGram <= 0)
            throw new MedicationException(MedicationErrorMessage.INVALID_WEIGHT);
    }

    private void validateCode() {
        validateCode(this.code);
    }

    private static boolean isAllowedCharacterInCode(Character character) {
        boolean isUpperCaseAlphabeticCharacter = (Character.isAlphabetic(character) && Character.isUpperCase(character));
        return isUpperCaseAlphabeticCharacter || Character.isDigit(character) || character.equals('_');
    }
}
