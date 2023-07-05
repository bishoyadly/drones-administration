package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DatabaseMedicationGatewayIntegrationTests {

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    MedicationGatewayRequest medicationGatewayRequest;

    MedicationRecord medicationRecord;

    MedicationGatewayRequest buildMedicationGatewayRequest() {
        MedicationGatewayRequest request = new MedicationGatewayRequest();
        request.setCode("ABC_123");
        request.setName("Medicine-123_1mg");
        request.setWeightInGram(10);
        request.setImageUrl("https://test-image-url.com");
        return request;
    }

    MedicationRecord buildMedicationRecord() {
        MedicationRecord record = new MedicationRecord();
        record.setCode("ABC_123");
        record.setName("Medicine-123_1mg");
        record.setWeightInGram(10);
        record.setImageUrl("https://test-image-url.com");
        return record;
    }

    @BeforeEach
    void setUp() {
        medicationGatewayRequest = buildMedicationGatewayRequest();
        medicationRecord = buildMedicationRecord();
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    @Test
    void saveMedicationRecord() {
        MedicationRecord savedRecord = medicationRepository.save(medicationRecord);
        assertTrue(medicationRepository.existsById(medicationRecord.getCode()));
        assertNotNull(savedRecord);
    }

    @Test
    void deleteMedicationRecordByCode() {
        MedicationRecord savedRecord = medicationRepository.save(medicationRecord);
        medicationRepository.deleteById(medicationRecord.getCode());
        assertNotNull(savedRecord);
        assertFalse(medicationRepository.existsById(medicationRecord.getCode()));
    }

    @Test
    void findAllMedicationsByIds() {
        MedicationRecord savedRecord1 = medicationRepository.save(medicationRecord);
        medicationRecord.setCode("XYZ_123");
        MedicationRecord savedRecord2 = medicationRepository.save(medicationRecord);
        assertNotNull(savedRecord1);
        assertNotNull(savedRecord2);
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRepository.findAllById(List.of(savedRecord1.getCode(), savedRecord2.getCode())).forEach(medicationRecordList::add);
        assertEquals(2, medicationRecordList.size());
    }
}
