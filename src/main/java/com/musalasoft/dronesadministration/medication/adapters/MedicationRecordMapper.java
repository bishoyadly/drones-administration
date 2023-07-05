package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicationRecordMapper {
    MedicationRecord gatewayRequestToRecord(MedicationGatewayRequest request);

    MedicationGatewayResponse recordToGatewayResponse(MedicationRecord record);
}
