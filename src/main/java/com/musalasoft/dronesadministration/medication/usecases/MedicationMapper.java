package com.musalasoft.dronesadministration.medication.usecases;

import com.musalasoft.dronesadministration.medication.entities.MedicationEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MedicationMapper {

    MedicationResponse medicationRequestToMedicationResponse(MedicationRequest medicationGatewayModel);

    MedicationGatewayRequest medicationRequestToMedicationGatewayRequest(MedicationRequest medicationGatewayModel);

    MedicationEntity medicationRequestToMedicationEntity(MedicationRequest medicationGatewayModel);

    MedicationResponse medicationGatewayResponseToMedicationResponse(MedicationGatewayResponse expectedGatewayResponse);
}
