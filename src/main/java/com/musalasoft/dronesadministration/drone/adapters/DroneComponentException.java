package com.musalasoft.dronesadministration.drone.adapters;


import com.musalasoft.dronesadministration.model.ProblemDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneComponentException extends RuntimeException {
    final transient ProblemDto problemDto;

    public DroneComponentException(ProblemDto problemDto) {
        this.problemDto = problemDto;
    }
}
