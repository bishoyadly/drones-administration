package com.musalasoft.dronesadministration.drone.adapters.medicationloading;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapper;
import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGateway;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneNotFoundException;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecord;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecordMapper;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRepository;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class DatabaseMedicationLoadingGateway implements MedicationLoadingGateway {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneRecordMapper droneRecordMapper;
    private final MedicationRecordMapper medicationRecordMapper;

    @Autowired
    public DatabaseMedicationLoadingGateway(DroneRepository droneRepository, MedicationRepository medicationRepository,
                                            DroneRecordMapper droneRecordMapper,
                                            MedicationRecordMapper medicationRecordMapper) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.droneRecordMapper = droneRecordMapper;
        this.medicationRecordMapper = medicationRecordMapper;
    }

    @Override
    @Transactional
    public boolean droneExistsBySerialNumber(String serialNumber) {
        return droneRepository.existsById(serialNumber);
    }

    @Override
    @Transactional
    public boolean doMedicationsExistByCodesList(List<String> medicationCodesList) {
        for (String medicationCode : medicationCodesList) {
            boolean medicationDoesNotExist = !medicationRepository.existsById(medicationCode);
            if (medicationDoesNotExist)
                return false;
        }
        return true;
    }

    @Override
    public MedicationLoadingGatewayResponse updateDroneRecordWithMedications(MedicationLoadingGatewayRequest gatewayRequest) {
        try {
            return processUpdateDroneRecordWithMedications(gatewayRequest);
        } catch (DroneNotFoundException | MedicationNotFoundException exception) {
            return buildEmptyMedicationLoadingGatewayResponse();
        }
    }

    @Override
    @Transactional
    public MedicationLoadingGatewayResponse getDroneRecordBySerialNumber(String serialNumber) {
        Optional<DroneRecord> optional = droneRepository.findById(serialNumber);
        if (optional.isPresent()) {
            return droneRecordMapper.recordToMedicationLoadingGatewayResponse(optional.get());
        } else {
            return buildEmptyMedicationLoadingGatewayResponse();
        }
    }

    @Override
    @Transactional
    public List<MedicationGatewayResponse> getMedicationRecordsByCodesList(List<String> medicationCodesList) {
        List<MedicationGatewayResponse> medicationGatewayResponseList = new LinkedList<>();
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRepository.findAllById(medicationCodesList).forEach(medicationRecordList::add);
        for (MedicationRecord medicationRecord : medicationRecordList) {
            medicationGatewayResponseList.add(medicationRecordMapper.recordToGatewayResponse(medicationRecord));
        }
        return medicationGatewayResponseList;
    }

    @Override
    @Transactional
    public MedicationLoadingGatewayResponsePage getDronesPaginated(MedicationLoadingGatewayRequest gatewayRequest) {
        Page<DroneRecord> droneRecordPage = droneRepository
                .findAll(PageRequest.of(gatewayRequest.getDronesPageNumber(), gatewayRequest.getDronesPageSize()));
        return buildMedicationLoadingGatewayResponsePage(droneRecordPage);
    }

    MedicationLoadingGatewayResponsePage buildMedicationLoadingGatewayResponsePage(Page<DroneRecord> droneRecordPage) {
        List<DroneRecord> droneRecordList = new LinkedList<>();
        droneRecordPage.forEach(droneRecordList::add);
        MedicationLoadingGatewayResponsePage gatewayResponsePage = new MedicationLoadingGatewayResponsePage();
        gatewayResponsePage.setPageNumber(droneRecordPage.getNumber());
        gatewayResponsePage.setPageSize(droneRecordPage.getSize());
        Long totalElements = droneRecordPage.getTotalElements();
        gatewayResponsePage.setTotalElements(totalElements.intValue());
        List<MedicationLoadingGatewayResponse> medicationLoadingGatewayResponseList = droneRecordMapper
                .recordListToMedicationLoadingGatewayResponseList(droneRecordList);
        gatewayResponsePage.setMedicationLoadingGatewayResponseList(medicationLoadingGatewayResponseList);
        return gatewayResponsePage;
    }

    private MedicationLoadingGatewayResponse processUpdateDroneRecordWithMedications(MedicationLoadingGatewayRequest gatewayRequest) {
        DroneRecord droneRecord = getDroneRecord(gatewayRequest.getDroneSerialNumber());
        List<MedicationRecord> medicationRecordList = getMedicationRecordList(gatewayRequest.getMedicationCodesList());
        droneRecord.setMedicationRecordList(mergeMedicationRecordLists(droneRecord.getMedicationRecordList(), medicationRecordList));
        droneRecord.setState(gatewayRequest.getDroneState());
        DroneRecord savedRecord = droneRepository.save(droneRecord);
        return droneRecordMapper.recordToMedicationLoadingGatewayResponse(savedRecord);
    }

    private List<MedicationRecord> mergeMedicationRecordLists(List<MedicationRecord> savedList,
                                                              List<MedicationRecord> newList) {
        Set<MedicationRecord> medicationRecordSet = new HashSet<>();
        medicationRecordSet.addAll(savedList);
        medicationRecordSet.addAll(newList);
        return new LinkedList<>(medicationRecordSet);
    }

    private DroneRecord getDroneRecord(String serialNumber) {
        Optional<DroneRecord> optional = droneRepository.findById(serialNumber);
        if (optional.isPresent())
            return optional.get();
        else
            throw new DroneNotFoundException(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST);
    }

    private List<MedicationRecord> getMedicationRecordList(List<String> medicationCodesList) {
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRepository.findAllById(medicationCodesList).forEach(medicationRecordList::add);
        return medicationRecordList;
    }

    private MedicationLoadingGatewayResponse buildEmptyMedicationLoadingGatewayResponse() {
        MedicationLoadingGatewayResponse gatewayResponse = new MedicationLoadingGatewayResponse();
        gatewayResponse.setDroneSerialNumber("");
        gatewayResponse.setDroneModel("");
        gatewayResponse.setDroneLoadWeightLimitInGram(0);
        gatewayResponse.setDroneBatteryCapacityInPercentage(0);
        gatewayResponse.setDroneState("");
        gatewayResponse.setMedicationGatewayResponseList(new ArrayList<>());
        return gatewayResponse;
    }
}
