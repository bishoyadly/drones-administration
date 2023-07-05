package com.musalasoft.dronesadministration.drone.adapters.registration;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapper;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapperImpl;
import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGateway;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayResponse;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseDroneRegistrationGatewayTests {

    @Mock
    DroneRepository droneRepository;

    DroneRecordMapper droneRecordMapper;

    DroneRegistrationGateway droneRegistrationGateway;

    DroneRecord buildDroneRecord() {
        DroneRecord record = new DroneRecord();
        record.setSerialNumber("ABCDE123");
        record.setModel("HEAVY_WEIGHT");
        record.setLoadWeightLimitInGram(300);
        record.setBatteryCapacityInPercentage(100);
        record.setState("IDLE");
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRecordList.add(0, buildMedicationResponse("ABC_123", "medicine1"));
        medicationRecordList.add(0, buildMedicationResponse("XYZ_123", "medicine2"));
        record.setMedicationRecordList(medicationRecordList);
        return record;
    }

    MedicationRecord buildMedicationResponse(String code, String name) {
        MedicationRecord response = new MedicationRecord();
        response.setCode(code);
        response.setName(name);
        response.setWeightInGram(10);
        response.setImageUrl("https://imageUrl.com");
        return response;
    }

    DroneRegistrationGatewayRequest buildDroneRegistrationGatewayRequest() {
        DroneRegistrationGatewayRequest record = new DroneRegistrationGatewayRequest();
        record.setSerialNumber("ABCDE123");
        record.setModel("HEAVY_WEIGHT");
        record.setLoadWeightLimitInGram(300);
        record.setBatteryCapacityInPercentage(100);
        record.setState("IDLE");
        return record;
    }

    DroneRegistrationGatewayResponse buildEmptyDroneRegistrationGatewayResponse() {
        DroneRegistrationGatewayResponse gatewayResponse = new DroneRegistrationGatewayResponse();
        gatewayResponse.setSerialNumber("");
        gatewayResponse.setModel("");
        gatewayResponse.setLoadWeightLimitInGram(0);
        gatewayResponse.setBatteryCapacityInPercentage(0);
        gatewayResponse.setState("");
        return gatewayResponse;
    }


    DroneRegistrationGatewayResponse buildDroneRegistrationGatewayResponse() {
        DroneRegistrationGatewayResponse gatewayResponse = new DroneRegistrationGatewayResponse();
        gatewayResponse.setSerialNumber("ABCDE123");
        gatewayResponse.setModel("HEAVY_WEIGHT");
        gatewayResponse.setLoadWeightLimitInGram(300);
        gatewayResponse.setBatteryCapacityInPercentage(100);
        gatewayResponse.setState("IDLE");
        return gatewayResponse;
    }

    void verifySaveDroneRecordArgument(DroneRecord expectedRecord) {
        ArgumentCaptor<DroneRecord> argumentCaptor = ArgumentCaptor.forClass(DroneRecord.class);
        verify(droneRepository, times(1)).save(argumentCaptor.capture());
        assertDroneRegistrationGatewayRequestFields(expectedRecord, argumentCaptor.getValue());
    }

    void assertDroneRegistrationGatewayRequestFields(DroneRecord expected, DroneRecord actual) {
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getLoadWeightLimitInGram(), actual.getLoadWeightLimitInGram());
        assertEquals(expected.getBatteryCapacityInPercentage(), actual.getBatteryCapacityInPercentage());
        assertEquals(expected.getState(), actual.getState());
    }

    void assertDroneRegistrationGatewayResponse(DroneRegistrationGatewayResponse expected, DroneRegistrationGatewayResponse actual) {
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getLoadWeightLimitInGram(), actual.getLoadWeightLimitInGram());
        assertEquals(expected.getBatteryCapacityInPercentage(), actual.getBatteryCapacityInPercentage());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getMedicationGatewayResponseList().size(), expected.getMedicationGatewayResponseList().size());
    }

    void assertEmptyDroneRegistrationGatewayResponse(DroneRegistrationGatewayResponse gatewayResponse) {
        assertEquals("", gatewayResponse.getSerialNumber());
        assertEquals("", gatewayResponse.getModel());
        assertEquals(0, gatewayResponse.getLoadWeightLimitInGram());
        assertEquals(0, gatewayResponse.getBatteryCapacityInPercentage());
        assertEquals("", gatewayResponse.getState());
        assertEquals(0, gatewayResponse.getMedicationGatewayResponseList().size());
    }

    @BeforeEach
    void setUp() {
        droneRecordMapper = new DroneRecordMapperImpl();
        droneRegistrationGateway = new DatabaseDroneRegistrationGateway(droneRepository, droneRecordMapper);
    }

    @Test
    void droneExistsBySerialNumber() {
        String droneSerialNumber = "ABCDE123";
        droneRegistrationGateway.droneExistsBySerialNumber(droneSerialNumber);
        verify(droneRepository, times(1)).existsById(droneSerialNumber);
    }

    @Test
    void saveDroneRecord() {
        DroneRegistrationGatewayRequest gatewayRequest = buildDroneRegistrationGatewayRequest();
        DroneRecord expectedRecord = droneRecordMapper.gatewayRequestToDroneRecord(gatewayRequest);
        droneRegistrationGateway.saveDroneRecord(gatewayRequest);
        verifySaveDroneRecordArgument(expectedRecord);
    }

    @Test
    void getDroneRecordBySerialNumber_caseDoesNotExist() {
        String droneSerialNumber = "ABCDE123";
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.empty());

        DroneRegistrationGatewayResponse gatewayResponse = droneRegistrationGateway.getDroneRecordBySerialNumber(droneSerialNumber);

        verify(droneRepository, times(1)).findById(droneSerialNumber);
        assertEmptyDroneRegistrationGatewayResponse(gatewayResponse);
    }

    @Test
    void getDroneRecordBySerialNumber_caseExists() {
        String droneSerialNumber = "ABCDE123";
        DroneRecord droneRecord = buildDroneRecord();
        DroneRegistrationGatewayResponse expectedGatewayResponse = droneRecordMapper.droneRecordToGatewayResponse(droneRecord);
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.of(droneRecord));

        DroneRegistrationGatewayResponse gatewayResponse = droneRegistrationGateway.getDroneRecordBySerialNumber(droneSerialNumber);

        verify(droneRepository, times(1)).findById(droneSerialNumber);
        assertDroneRegistrationGatewayResponse(expectedGatewayResponse, gatewayResponse);
    }

    @Test
    void deleteDroneRecordBySerialNumber() {
        String droneSerialNumber = "ABCDE123";
        droneRegistrationGateway.deleteDroneRecordBySerialNumber(droneSerialNumber);
        verify(droneRepository, times(1)).deleteById(droneSerialNumber);
    }
}
