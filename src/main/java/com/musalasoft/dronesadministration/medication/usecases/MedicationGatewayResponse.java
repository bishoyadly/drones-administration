package com.musalasoft.dronesadministration.medication.usecases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationGatewayResponse {
    private String code;
    private String name;
    private Integer weightInGram;
    private String imageUrl;
}