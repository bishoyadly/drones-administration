package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGateway;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseMedicationGateway implements MedicationGateway {

    private final MedicationRepository medicationRepository;
    private final MedicationRecordMapper medicationRecordMapper;

    @Autowired
    public DatabaseMedicationGateway(MedicationRepository medicationRepository, MedicationRecordMapper medicationRecordMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationRecordMapper = medicationRecordMapper;
    }

    @Override
    public boolean medicationExistsByCode(String code) {
        return medicationRepository.existsById(code);
    }

    @Override
    public void saveMedicationRecord(MedicationGatewayRequest gatewayRequest) {
        MedicationRecord medicationRecord = medicationRecordMapper.gatewayRequestToRecord(gatewayRequest);
        medicationRepository.save(medicationRecord);
    }

    @Override
    public MedicationGatewayResponse getMedicationRecordByCode(String code) {
        Optional<MedicationRecord> option = medicationRepository.findById(code);
        if (option.isPresent())
            return medicationRecordMapper.recordToGatewayResponse(option.get());
        else
            return buildNullMedicationGatewayResponse();
    }

    @Override
    public void deleteMedicationRecordByCode(String code) {
        medicationRepository.deleteById(code);
    }

    private MedicationGatewayResponse buildNullMedicationGatewayResponse() {
        MedicationGatewayResponse response = new MedicationGatewayResponse();
        response.setCode("");
        response.setName("");
        response.setWeightInGram(0);
        response.setImageUrl("");
        return response;
    }
}
