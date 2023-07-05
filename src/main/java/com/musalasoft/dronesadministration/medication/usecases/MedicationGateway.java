package com.musalasoft.dronesadministration.medication.usecases;

public interface MedicationGateway {
    boolean medicationExistsByCode(String code);

    void saveMedicationRecord(MedicationGatewayRequest medicationGatewayRequest);

    MedicationGatewayResponse getMedicationRecordByCode(String code);

    void deleteMedicationRecordByCode(String code);
}
