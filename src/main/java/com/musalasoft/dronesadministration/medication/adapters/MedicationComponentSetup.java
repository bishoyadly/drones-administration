package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGateway;
import com.musalasoft.dronesadministration.medication.usecases.MedicationInputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationOutputBoundary;
import com.musalasoft.dronesadministration.medication.usecases.MedicationUseCaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MedicationComponentSetup {

    @Bean
    MedicationInputBoundary medicationInputBoundary(MedicationGateway medicationGateway,
                                                    MedicationOutputBoundary medicationOutputBoundary) {
        return MedicationUseCaseFactory.buildMedicationInputBoundary(medicationGateway, medicationOutputBoundary);
    }
}
