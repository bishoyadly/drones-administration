package com.musalasoft.dronesadministration.drone.adapters.medicationloading;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapper;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapperImpl;
import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGateway;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayResponsePage;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecord;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecordMapper;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecordMapperImpl;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRepository;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseMedicationLoadingGatewayTests {

    @Mock
    MedicationRepository medicationRepository;

    @Mock
    DroneRepository droneRepository;

    DroneRecordMapper droneRecordMapper;

    MedicationRecordMapper medicationRecordMapper;

    MedicationLoadingGateway gateway;

    MedicationLoadingGatewayRequest gatewayRequest;

    DroneRecord droneRecord;

    MedicationLoadingGatewayRequest buildMedicationLoadingGatewayRequest() {
        MedicationLoadingGatewayRequest gatewayRequest = new MedicationLoadingGatewayRequest();
        gatewayRequest.setDroneSerialNumber("ABC12345");
        gatewayRequest.setMedicationCodesList(List.of("ABC_123", "XYZ_123"));
        gatewayRequest.setDroneState(DroneEntity.State.LOADING.name());
        return gatewayRequest;
    }

    DroneRecord buildDroneRecord() {
        DroneRecord record = new DroneRecord();
        record.setSerialNumber("ABCDE123");
        record.setModel("HEAVY_WEIGHT");
        record.setLoadWeightLimitInGram(300);
        record.setBatteryCapacityInPercentage(100);
        record.setState("IDLE");
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRecordList.add(buildMedicationRecord("ABC_123", "medicine1"));
        medicationRecordList.add(buildMedicationRecord("XYZ_123", "medicine2"));
        record.setMedicationRecordList(medicationRecordList);
        return record;
    }

    MedicationRecord buildMedicationRecord(String code, String name) {
        MedicationRecord record = new MedicationRecord();
        record.setCode(code);
        record.setName(name);
        record.setWeightInGram(10);
        record.setImageUrl("https://imageUrl.com");
        return record;
    }

    void verifySaveDroneRecordArgument(DroneRecord expectedRecord) {
        ArgumentCaptor<DroneRecord> argumentCaptor = ArgumentCaptor.forClass(DroneRecord.class);
        verify(droneRepository, times(1)).save(argumentCaptor.capture());
        assertDroneRecordFields(expectedRecord, argumentCaptor.getValue());
    }

    void assertDroneRecordFields(DroneRecord expected, DroneRecord actual) {
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getLoadWeightLimitInGram(), actual.getLoadWeightLimitInGram());
        assertEquals(expected.getBatteryCapacityInPercentage(), actual.getBatteryCapacityInPercentage());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getModifiedDate(), actual.getModifiedDate());
        assertEquals(expected.getMedicationRecordList().size(), actual.getMedicationRecordList().size());
    }

    void assertEmptyGatewayResponse(MedicationLoadingGatewayResponse actual) {
        assertEquals("", actual.getDroneSerialNumber());
        assertEquals("", actual.getDroneModel());
        assertEquals(0, actual.getDroneLoadWeightLimitInGram());
        assertEquals(0, actual.getDroneBatteryCapacityInPercentage());
        assertEquals("", actual.getDroneState());
        assertEquals(0, actual.getMedicationGatewayResponseList().size());
    }

    void assertGatewayResponse(MedicationLoadingGatewayResponse expected, MedicationLoadingGatewayResponse actual) {
        assertEquals(expected.getDroneSerialNumber(), actual.getDroneSerialNumber());
        assertEquals(expected.getDroneModel(), actual.getDroneModel());
        assertEquals(expected.getDroneLoadWeightLimitInGram(), actual.getDroneLoadWeightLimitInGram());
        assertEquals(expected.getDroneBatteryCapacityInPercentage(), actual.getDroneBatteryCapacityInPercentage());
        assertEquals(expected.getDroneState(), actual.getDroneState());
        assertEquals(expected.getMedicationGatewayResponseList().size(), actual.getMedicationGatewayResponseList().size());
    }

    @BeforeEach
    void setUp() {
        droneRecordMapper = new DroneRecordMapperImpl();
        medicationRecordMapper = new MedicationRecordMapperImpl();
        gateway = new DatabaseMedicationLoadingGateway(droneRepository, medicationRepository, droneRecordMapper, medicationRecordMapper);
        gatewayRequest = buildMedicationLoadingGatewayRequest();
        droneRecord = buildDroneRecord();
    }

    @Test
    void droneExistsBySerialNumber() {
        String droneSerialNumber = "ABC123";
        gateway.droneExistsBySerialNumber(droneSerialNumber);
        verify(droneRepository, times(1)).existsById(droneSerialNumber);
    }

    @Test
    void doMedicationsExistByCodesList_caseFirstDoesNotExist() {
        List<String> medicationCodesList = List.of("ABC_123", "XYZ_123");
        when(medicationRepository.existsById(medicationCodesList.get(0))).thenReturn(false);
        gateway.doMedicationsExistByCodesList(medicationCodesList);
        verify(medicationRepository, times(1)).existsById(medicationCodesList.get(0));
    }

    @Test
    void doMedicationsExistByCodesList_caseLastOneDoesNotExist() {
        List<String> medicationCodesList = List.of("ABC_123", "XYZ_123");
        when(medicationRepository.existsById(medicationCodesList.get(0))).thenReturn(true);
        gateway.doMedicationsExistByCodesList(medicationCodesList);
        verify(medicationRepository, times(2)).existsById(anyString());
    }


    @Test
    void updateDroneRecordWithMedications_caseDroneDoesNotExist() {
        when(droneRepository.findById(any())).thenReturn(Optional.empty());

        MedicationLoadingGatewayResponse gatewayResponse = gateway.updateDroneRecordWithMedications(gatewayRequest);

        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        assertEmptyGatewayResponse(gatewayResponse);
    }

    @Test
    void updateDroneRecordWithMedications_caseMedicationDoesNotExist() {
        droneRecord.setState(gatewayRequest.getDroneState());
        MedicationLoadingGatewayResponse expectedGatewayResponse = droneRecordMapper.recordToMedicationLoadingGatewayResponse(droneRecord);
        when(droneRepository.findById(any())).thenReturn(Optional.of(droneRecord));
        when(medicationRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(droneRepository.save(any())).thenReturn(droneRecord);

        MedicationLoadingGatewayResponse gatewayResponse = gateway.updateDroneRecordWithMedications(gatewayRequest);

        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        verify(medicationRepository, times(1)).findAllById(any());
        assertGatewayResponse(expectedGatewayResponse, gatewayResponse);
    }

    @Test
    void updateDroneRecordWithMedications_caseNoMedicationWasLoaded() {
        DroneRecord recordWithoutMedications = buildDroneRecord();
        recordWithoutMedications.setMedicationRecordList(new LinkedList<>());
        droneRecord.setState(gatewayRequest.getDroneState());
        MedicationLoadingGatewayResponse expectedGatewayResponse = droneRecordMapper.recordToMedicationLoadingGatewayResponse(droneRecord);
        when(medicationRepository.findAllById(any())).thenReturn(droneRecord.getMedicationRecordList());
        when(droneRepository.findById(any())).thenReturn(Optional.of(recordWithoutMedications));
        when(droneRepository.save(any())).thenReturn(droneRecord);

        MedicationLoadingGatewayResponse actualGatewayResponse = gateway.updateDroneRecordWithMedications(gatewayRequest);

        verify(medicationRepository, times(1)).findAllById(anyList());
        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        verifySaveDroneRecordArgument(droneRecord);
        assertGatewayResponse(expectedGatewayResponse, actualGatewayResponse);
    }

    @Test
    void updateDroneRecordWithMedications_caseSomeMedicationsWereLoaded() {
        droneRecord.setState(gatewayRequest.getDroneState());
        MedicationLoadingGatewayResponse expectedGatewayResponse = droneRecordMapper.recordToMedicationLoadingGatewayResponse(droneRecord);
        when(medicationRepository.findAllById(any())).thenReturn(droneRecord.getMedicationRecordList());
        when(droneRepository.findById(any())).thenReturn(Optional.of(droneRecord));
        when(droneRepository.save(any())).thenReturn(droneRecord);

        MedicationLoadingGatewayResponse actualGatewayResponse = gateway.updateDroneRecordWithMedications(gatewayRequest);

        verify(medicationRepository, times(1)).findAllById(anyList());
        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        verifySaveDroneRecordArgument(droneRecord);
        assertGatewayResponse(expectedGatewayResponse, actualGatewayResponse);
    }

    @Test
    void getDroneRecordBySerialNumber_caseDroneDoesNotExist() {
        when(droneRepository.findById(any())).thenReturn(Optional.empty());

        MedicationLoadingGatewayResponse gatewayResponse = gateway.getDroneRecordBySerialNumber(gatewayRequest.getDroneSerialNumber());

        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        assertEmptyGatewayResponse(gatewayResponse);
    }

    @Test
    void getDroneRecordBySerialNumber_caseExists() {
        MedicationLoadingGatewayResponse expectedGatewayResponse = droneRecordMapper.recordToMedicationLoadingGatewayResponse(droneRecord);
        when(droneRepository.findById(any())).thenReturn(Optional.of(droneRecord));

        MedicationLoadingGatewayResponse gatewayResponse = gateway.getDroneRecordBySerialNumber(gatewayRequest.getDroneSerialNumber());

        verify(droneRepository, times(1)).findById(gatewayRequest.getDroneSerialNumber());
        assertGatewayResponse(expectedGatewayResponse, gatewayResponse);
    }

    @Test
    void getMedicationRecordsByCodesList_caseMedicationDoesNotExist() {
        Iterable<MedicationRecord> medicationRecordIterable = new LinkedList<>();
        when(medicationRepository.findAllById(any())).thenReturn(medicationRecordIterable);
        List<MedicationGatewayResponse> gatewayResponse = gateway.getMedicationRecordsByCodesList(gatewayRequest.getMedicationCodesList());
        verify(medicationRepository, times(1)).findAllById(gatewayRequest.getMedicationCodesList());
        assertEquals(0, gatewayResponse.size());
    }

    @Test
    void getMedicationRecordsByCodesList_caseMedicationsExist() {
        List<MedicationRecord> medicationRecordList = List.of(buildMedicationRecord("ABC_123", "Medicine1"),
                buildMedicationRecord("XYZ_123", "Medicine2"));
        when(medicationRepository.findAllById(any())).thenReturn(medicationRecordList);
        List<MedicationGatewayResponse> gatewayResponse = gateway.getMedicationRecordsByCodesList(gatewayRequest.getMedicationCodesList());
        verify(medicationRepository, times(1)).findAllById(gatewayRequest.getMedicationCodesList());
        assertEquals(2, gatewayResponse.size());
    }

    @Test
    void getDronesPaginated_caseEmptyPage() {
        gatewayRequest.setDronesPageNumber(0);
        gatewayRequest.setDronesPageSize(10);
        Page<DroneRecord> droneRecordPage = Page.empty();
        when(droneRepository.findAll(any(PageRequest.class))).thenReturn(droneRecordPage);

        MedicationLoadingGatewayResponsePage gatewayResponsePage = gateway.getDronesPaginated(gatewayRequest);

        verify(droneRepository, times(1)).findAll(PageRequest.of(gatewayRequest.getDronesPageNumber(),
                gatewayRequest.getDronesPageSize()));
        assertEquals(0, gatewayResponsePage.getMedicationLoadingGatewayResponseList().size());
    }

    @Test
    void getDronesPaginated_casePageWithRecords() {
        gatewayRequest.setDronesPageNumber(0);
        gatewayRequest.setDronesPageSize(10);
        Page<DroneRecord> droneRecordPage = new PageImpl<>(List.of(droneRecord, droneRecord));
        when(droneRepository.findAll(any(PageRequest.class))).thenReturn(droneRecordPage);

        MedicationLoadingGatewayResponsePage gatewayResponsePage = gateway.getDronesPaginated(gatewayRequest);

        verify(droneRepository, times(1)).findAll(PageRequest.of(gatewayRequest.getDronesPageNumber(),
                gatewayRequest.getDronesPageSize()));
        assertEquals(2, gatewayResponsePage.getMedicationLoadingGatewayResponseList().size());
    }
}
