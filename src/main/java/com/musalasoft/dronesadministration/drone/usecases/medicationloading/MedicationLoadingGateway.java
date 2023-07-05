package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;

import java.util.List;

public interface MedicationLoadingGateway {
    boolean droneExistsBySerialNumber(String serialNumber);

    boolean doMedicationsExistByCodesList(List<String> medicationCodesList);

    MedicationLoadingGatewayResponse updateDroneRecordWithMedications(MedicationLoadingGatewayRequest gatewayRequest);

    MedicationLoadingGatewayResponse getDroneRecordBySerialNumber(String serialNumber);

    List<MedicationGatewayResponse> getMedicationRecordsByCodesList(List<String> medicationCodesList);

    MedicationLoadingGatewayResponsePage getDronesPaginated(MedicationLoadingGatewayRequest gatewayRequest);
}
