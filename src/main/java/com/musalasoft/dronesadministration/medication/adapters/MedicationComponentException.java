package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.model.ProblemDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationComponentException extends RuntimeException {

    final transient ProblemDto problemDto;

    public MedicationComponentException(ProblemDto problemDto) {
        this.problemDto = problemDto;
    }
}
