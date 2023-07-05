package com.musalasoft.dronesadministration.drone.adapters.registration;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRecord;
import com.musalasoft.dronesadministration.medication.adapters.MedicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DatabaseDroneRegistrationGatewayIntegrationTests {

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    DroneRecord droneRecord;

    DroneRecord buildDroneRecord() {
        DroneRecord record = new DroneRecord();
        record.setSerialNumber("ABCDE123");
        record.setModel("HEAVY_WEIGHT");
        record.setLoadWeightLimitInGram(300);
        record.setBatteryCapacityInPercentage(100);
        record.setState("IDLE");
        List<MedicationRecord> medicationRecordList = new LinkedList<>();
        medicationRecordList.add(0, buildMedicationRecord("ABC_123", "medicine1"));
        medicationRecordList.add(0, buildMedicationRecord("XYZ_123", "medicine2"));
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

    @BeforeEach
    void setUp() {
        droneRecord = buildDroneRecord();
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    @Test
    void saveDroneRecord() {
        medicationRepository.saveAll(droneRecord.getMedicationRecordList());
        DroneRecord savedRecord = droneRepository.save(droneRecord);
        assertTrue(droneRepository.existsById(droneRecord.getSerialNumber()));
        assertNotNull(savedRecord);
    }

    @Test
    void deleteDroneRecord() {
        medicationRepository.saveAll(droneRecord.getMedicationRecordList());
        DroneRecord savedRecord = droneRepository.save(droneRecord);
        droneRepository.deleteById(droneRecord.getSerialNumber());
        assertNotNull(savedRecord);
        assertFalse(droneRepository.existsById(droneRecord.getSerialNumber()));
    }

    @Test
    void findAllPaginated() {
        medicationRepository.saveAll(droneRecord.getMedicationRecordList());
        DroneRecord droneRecord1 = buildDroneRecord();
        droneRecord1.setSerialNumber("ABC123");
        DroneRecord droneRecord2 = buildDroneRecord();
        droneRecord2.setSerialNumber("XYZ123");
        DroneRecord droneRecord3 = buildDroneRecord();
        droneRecord3.setSerialNumber("GHI123");
        DroneRecord savedRecord1 = droneRepository.save(droneRecord1);
        DroneRecord savedRecord2 = droneRepository.save(droneRecord2);
        DroneRecord savedRecord3 = droneRepository.save(droneRecord3);

        Page<DroneRecord> droneRecordPage = droneRepository.findAll(PageRequest.of(0, 2));

        assertNotNull(savedRecord1);
        assertNotNull(savedRecord2);
        assertNotNull(savedRecord3);
        assertEquals(2, droneRecordPage.getNumberOfElements());
    }
}
