package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.medication.usecases.MedicationGateway;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationGatewayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseMedicationGatewayTests {

    @Mock
    MedicationRepository medicationRepository;

    MedicationRecordMapper medicationRecordMapper;

    MedicationGateway medicationGateway;

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

    private void assertSaveRecordArgument(MedicationRecord expectedMedicationRecord) {
        ArgumentCaptor<MedicationRecord> argumentCaptor = ArgumentCaptor.forClass(MedicationRecord.class);
        verify(medicationRepository, times(1)).save(argumentCaptor.capture());
        assertMedicationRecordFields(expectedMedicationRecord, argumentCaptor.getValue());
    }

    private void assertMedicationRecordFields(MedicationRecord expected, MedicationRecord actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWeightInGram(), actual.getWeightInGram());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    private void assertMedicationGatewayResponseFields(MedicationGatewayResponse expected, MedicationGatewayResponse actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWeightInGram(), actual.getWeightInGram());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    private void assertNullMedicationGatewayResponse(MedicationGatewayResponse response) {
        assertEquals("", response.getCode());
        assertEquals("", response.getName());
        assertEquals(0, response.getWeightInGram());
        assertEquals("", response.getImageUrl());
    }

    @BeforeEach
    void setUp() {
        medicationRecordMapper = new MedicationRecordMapperImpl();
        medicationGateway = new DatabaseMedicationGateway(medicationRepository, medicationRecordMapper);
    }

    @Test
    void medicationExistsByCode() {
        String medicationCode = "ABC_123";
        medicationGateway.medicationExistsByCode(medicationCode);
        verify(medicationRepository, times(1)).existsById(medicationCode);
    }

    @Test
    void saveMedicationRecord() {
        MedicationGatewayRequest request = buildMedicationGatewayRequest();
        MedicationRecord expectedMedicationRecord = medicationRecordMapper.gatewayRequestToRecord(request);
        medicationGateway.saveMedicationRecord(request);
        assertSaveRecordArgument(expectedMedicationRecord);
    }

    @Test
    void getMedicationRecordByCode_caseRecordDoesNotExist() {
        String medicationCode = "ABC_123";
        when(medicationRepository.findById(medicationCode)).thenReturn(Optional.empty());

        MedicationGatewayResponse gatewayResponse = medicationGateway.getMedicationRecordByCode(medicationCode);

        verify(medicationRepository, times(1)).findById(medicationCode);
        assertNullMedicationGatewayResponse(gatewayResponse);
    }

    @Test
    void getMedicationRecordByCode_caseRecordExists() {
        String medicationCode = "ABC_123";
        MedicationRecord record = buildMedicationRecord();
        MedicationGatewayResponse expectedGatewayResponse = medicationRecordMapper.recordToGatewayResponse(record);
        when(medicationRepository.findById(medicationCode)).thenReturn(Optional.of(record));

        MedicationGatewayResponse gatewayResponse = medicationGateway.getMedicationRecordByCode(medicationCode);

        verify(medicationRepository, times(1)).findById(medicationCode);
        assertMedicationGatewayResponseFields(expectedGatewayResponse, gatewayResponse);
    }

    @Test
    void deleteMedicationRecordByCode() {
        String medicationCode = "ABC_123";
        medicationGateway.deleteMedicationRecordByCode(medicationCode);
        verify(medicationRepository, times(1)).deleteById(medicationCode);
    }
}
