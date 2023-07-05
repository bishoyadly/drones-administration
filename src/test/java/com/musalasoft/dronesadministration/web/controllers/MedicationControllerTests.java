package com.musalasoft.dronesadministration.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.dronesadministration.medication.adapters.MedicationComponent;
import com.musalasoft.dronesadministration.medication.adapters.MedicationComponentException;
import com.musalasoft.dronesadministration.medication.entities.MedicationErrorMessage;
import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.model.MedicationModelDto;
import com.musalasoft.dronesadministration.model.ProblemDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(value = {MedicationController.class, ControllerMapper.class})
class MedicationControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ControllerMapper controllerMapper;

    @MockBean
    MedicationComponent medicationComponent;

    private final String MEDICATION_API_BASE_URL = "/api/v1/medication";


    MedicationModelDto buildMedicationModelDto() {
        MedicationModelDto requestDto = new MedicationModelDto();
        requestDto.setCode("ABC_123");
        requestDto.setName("Medicine-123_1mg");
        requestDto.setWeightInGram(10);
        requestDto.setImageUrl("https://test-image-url.com");
        return requestDto;
    }

    MedicationResponse buildMedicationResponse() {
        MedicationResponse response = new MedicationResponse();
        response.setCode("ABC_123");
        response.setName("Medicine-123_1mg");
        response.setWeightInGram(10);
        response.setImageUrl("https://test-image-url.com");
        return response;
    }

    @SneakyThrows
    private MockHttpServletResponse performPostRequest(String requestUrl, MedicationModelDto request) {
        String requestBody = objectToJson(request);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn().getResponse();
    }

    @SneakyThrows
    private MockHttpServletResponse performPutRequest(String requestUrl, MedicationModelDto request) {
        String requestBody = objectToJson(request);
        return mockMvc.perform(
                        MockMvcRequestBuilders.put(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn().getResponse();
    }

    @SneakyThrows
    private MockHttpServletResponse performGetRequest(String requestUrl) {
        return mockMvc.perform(
                        MockMvcRequestBuilders.get(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    @SneakyThrows
    private MockHttpServletResponse performDeleteRequest(String requestUrl) {
        return mockMvc.perform(
                        MockMvcRequestBuilders.delete(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    @SneakyThrows
    static String objectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @BeforeEach
    void setUp() {
    }

    MedicationComponentException buildMedicationComponentException(Integer httpStatus, String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(httpStatus);
        problemDto.setDetailedErrorReason(errorMessage);
        return new MedicationComponentException(problemDto);
    }

    @Test
    void addMedication_caseInvalidRequest() throws UnsupportedEncodingException {
        MedicationModelDto request = buildMedicationModelDto();
        request.setCode("@#$abc");
        when(medicationComponent.addMedication(any())).thenThrow(buildMedicationComponentException(400, MedicationErrorMessage.INVALID_CODE));

        MockHttpServletResponse response = performPostRequest(MEDICATION_API_BASE_URL, request);
        String responseBody = response.getContentAsString();

        verify(medicationComponent, times(1)).addMedication(any(MedicationRequest.class));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(responseBody.contains(MedicationErrorMessage.INVALID_CODE));
    }

    @Test
    void addMedication_caseValidRequest() {
        MedicationModelDto request = buildMedicationModelDto();
        MockHttpServletResponse response = performPostRequest(MEDICATION_API_BASE_URL, request);
        verify(medicationComponent, times(1)).addMedication(any(MedicationRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    void updateMedication_caseInValidRequest() throws UnsupportedEncodingException {
        MedicationModelDto requestBody = buildMedicationModelDto();
        when(medicationComponent.updateMedication(any())).thenThrow(buildMedicationComponentException(404,
                MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
        String invalidMedicationCode = "abc#$";
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, invalidMedicationCode);

        MockHttpServletResponse response = performPutRequest(requestUrl, requestBody);
        String responseBody = response.getContentAsString();

        verify(medicationComponent, times(1)).updateMedication(any(MedicationRequest.class));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
    }

    @Test
    void updateMedication_caseValidRequest() {
        MedicationModelDto requestBody = buildMedicationModelDto();
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, requestBody.getCode());
        MockHttpServletResponse response = performPutRequest(requestUrl, requestBody);
        verify(medicationComponent, times(1)).updateMedication(any(MedicationRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    void getMedicationByCode_caseInValidRequest() throws UnsupportedEncodingException {
        when(medicationComponent.getMedicationByCode(any())).thenThrow(buildMedicationComponentException(404,
                MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
        String medicationCode = "ABC_1235";
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, medicationCode);

        MockHttpServletResponse response = performGetRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(medicationComponent, times(1)).getMedicationByCode(medicationCode);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
    }

    @Test
    void getMedicationByCode_caseValidRequest() throws UnsupportedEncodingException {
        MedicationResponse medicationResponse = buildMedicationResponse();
        when(medicationComponent.getMedicationByCode(any())).thenReturn(medicationResponse);
        String medicationCode = "ABC_1235";
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, medicationCode);

        MockHttpServletResponse response = performGetRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(medicationComponent, times(1)).getMedicationByCode(medicationCode);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseBody.contains(medicationResponse.getName()));
    }


    @Test
    void deleteMedicationByCode_caseInValidRequest() throws UnsupportedEncodingException {
        when(medicationComponent.deleteMedicationByCode(any())).thenThrow(buildMedicationComponentException(404,
                MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
        String medicationCode = "ABC_1235";
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, medicationCode);

        MockHttpServletResponse response = performDeleteRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(medicationComponent, times(1)).deleteMedicationByCode(medicationCode);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(MedicationUseCaseErrorMessages.MEDICATION_DOES_NOT_EXISTS));
    }


    @Test
    void deleteMedicationByCode_caseValidRequest() {
        String medicationCode = "ABC_1235";
        String requestUrl = String.format("%s/%s", MEDICATION_API_BASE_URL, medicationCode);

        MockHttpServletResponse response = performDeleteRequest(requestUrl);

        verify(medicationComponent, times(1)).deleteMedicationByCode(medicationCode);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }
}
