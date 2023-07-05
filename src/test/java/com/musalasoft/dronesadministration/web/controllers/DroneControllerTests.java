package com.musalasoft.dronesadministration.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.dronesadministration.drone.adapters.DroneComponent;
import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import com.musalasoft.dronesadministration.drone.entities.DroneErrorMessages;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationUseCaseErrorMessages;
import com.musalasoft.dronesadministration.model.DroneModelDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(value = {DroneController.class, ControllerMapper.class})
class DroneControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ControllerMapper controllerMapper;

    @MockBean
    DroneComponent droneComponent;

    private final String DRONE_API_BASE_URL = "/api/v1/drone";


    DroneModelDto buildDroneModelDto() {
        DroneModelDto requestDto = new DroneModelDto();
        requestDto.setSerialNumber("ABC123");
        requestDto.setModel(DroneModelDto.ModelEnum.HEAVY_WEIGHT);
        requestDto.setLoadWeightLimitInGram(100);
        requestDto.setState(DroneModelDto.StateEnum.IDLE);
        return requestDto;
    }

    DroneRegistrationResponse buildDroneRegistrationResponse() {
        DroneRegistrationResponse response = new DroneRegistrationResponse();
        response.setSerialNumber("ABC123");
        response.setModel(DroneEntity.Model.HEAVY_WEIGHT.name());
        response.setLoadWeightLimitInGram(100);
        response.setState(DroneEntity.State.IDLE.name());
        return response;
    }

    MedicationLoadingResponse buildMedicationLoadingResponse() {
        MedicationLoadingResponse response = new MedicationLoadingResponse();
        response.setDroneSerialNumber("ABC123");
        response.setDroneModel(DroneEntity.Model.HEAVY_WEIGHT.name());
        response.setDroneLoadWeightLimitInGram(100);
        response.setDroneBatteryCapacityInPercentage(100);
        response.setDroneState(DroneEntity.State.IDLE.name());
        response.setMedicationResponseList(new ArrayList<>());
        return response;
    }

    @SneakyThrows
    private MockHttpServletResponse performPostRequest(String requestUrl, DroneModelDto request) {
        String requestBody = objectToJson(request);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn().getResponse();
    }

    @SneakyThrows
    private MockHttpServletResponse performPostRequest(String requestUrl, List<String> request) {
        String requestBody = objectToJson(request);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn().getResponse();
    }

    @SneakyThrows
    private MockHttpServletResponse performPutRequest(String requestUrl, DroneModelDto request) {
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
                                .queryParam("pageNumber", "0")
                                .queryParam("pageSize", "10")
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

    DroneComponentException buildDroneComponentException(Integer httpStatus, String errorMessage) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setHttpStatusCode(httpStatus);
        problemDto.setDetailedErrorReason(errorMessage);
        return new DroneComponentException(problemDto);
    }

    @Test
    void registerDrone_caseInvalidRequest() throws UnsupportedEncodingException {
        DroneModelDto request = buildDroneModelDto();
        request.setSerialNumber("@#$abc");
        when(droneComponent.registerDrone(any())).thenThrow(buildDroneComponentException(400, DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT));

        MockHttpServletResponse response = performPostRequest(DRONE_API_BASE_URL, request);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).registerDrone(any(DroneRegistrationRequest.class));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(responseBody.contains(DroneErrorMessages.INVALID_SERIAL_NUMBER_FORMAT));
    }

    @Test
    void registerDrone_caseValidRequest() {
        DroneModelDto request = buildDroneModelDto();
        MockHttpServletResponse response = performPostRequest(DRONE_API_BASE_URL, request);
        verify(droneComponent, times(1)).registerDrone(any(DroneRegistrationRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    void updateRegisteredDrone_caseInValidRequest() throws UnsupportedEncodingException {
        DroneModelDto requestBody = buildDroneModelDto();
        when(droneComponent.updateRegisteredDrone(any())).thenThrow(buildDroneComponentException(404,
                DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, requestBody.getSerialNumber());

        MockHttpServletResponse response = performPutRequest(requestUrl, requestBody);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).updateRegisteredDrone(any(DroneRegistrationRequest.class));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
    }

    @Test
    void updateRegisteredDrone_caseValidRequest() {
        DroneModelDto requestBody = buildDroneModelDto();
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, requestBody.getSerialNumber());
        MockHttpServletResponse response = performPutRequest(requestUrl, requestBody);
        verify(droneComponent, times(1)).updateRegisteredDrone(any(DroneRegistrationRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    void getDroneBySerialNumber_caseInValidRequest() throws UnsupportedEncodingException {
        when(droneComponent.getDroneBySerialNumber(any())).thenThrow(buildDroneComponentException(404,
                DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
        String droneSerialNumber = "ABC1235";
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, droneSerialNumber);

        MockHttpServletResponse response = performGetRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).getDroneBySerialNumber(droneSerialNumber);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
    }

    @Test
    void getDroneBySerialNumber_caseValidRequest() throws UnsupportedEncodingException {
        DroneRegistrationResponse droneRegistrationResponse = buildDroneRegistrationResponse();
        when(droneComponent.getDroneBySerialNumber(any())).thenReturn(droneRegistrationResponse);
        String droneSerialNumber = "ABC1235";
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, droneSerialNumber);

        MockHttpServletResponse response = performGetRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).getDroneBySerialNumber(droneSerialNumber);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseBody.contains(droneRegistrationResponse.getModel()));
    }


    @Test
    void deleteDroneBySerialNumber_caseInValidRequest() throws UnsupportedEncodingException {
        when(droneComponent.deleteDroneBySerialNumber(any())).thenThrow(buildDroneComponentException(404,
                DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
        String droneSerialNumber = "ABC1235";
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, droneSerialNumber);

        MockHttpServletResponse response = performDeleteRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).deleteDroneBySerialNumber(droneSerialNumber);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
    }


    @Test
    void deleteDroneBySerialNumber_caseValidRequest() {
        String droneSerialNumber = "ABC_1235";
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, droneSerialNumber);

        MockHttpServletResponse response = performDeleteRequest(requestUrl);

        verify(droneComponent, times(1)).deleteDroneBySerialNumber(droneSerialNumber);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void loadDroneWithMedications_caseInvalidRequest() throws UnsupportedEncodingException {
        String droneSerialNumber = "ABC1235";
        String requestUrl = String.format("%s/%s/%s", DRONE_API_BASE_URL, droneSerialNumber, "load");
        List<String> medicationCodesList = List.of("ABC_123", "XYZ_123");
        when(droneComponent.loadDroneWithMedications(any())).thenThrow(buildDroneComponentException(404,
                DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));

        MockHttpServletResponse response = performPostRequest(requestUrl, medicationCodesList);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).loadDroneWithMedications(any(MedicationLoadingRequest.class));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(responseBody.contains(DroneRegistrationUseCaseErrorMessages.DRONE_DOES_NOT_EXIST));
    }

    @Test
    void loadDroneWithMedications_caseValidRequest() throws UnsupportedEncodingException {
        String droneSerialNumber = "ABC1235";
        String requestUrl = String.format("%s/%s/%s", DRONE_API_BASE_URL, droneSerialNumber, "load");
        List<String> medicationCodesList = List.of("ABC_123", "XYZ_123");
        MedicationLoadingResponse medicationLoadingResponse = buildMedicationLoadingResponse();
        when(droneComponent.loadDroneWithMedications(any())).thenReturn(medicationLoadingResponse);

        MockHttpServletResponse response = performPostRequest(requestUrl, medicationCodesList);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).loadDroneWithMedications(any(MedicationLoadingRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseBody.contains(medicationLoadingResponse.getDroneModel()));
    }

    @Test
    void getDronesAvailableForLoading() throws UnsupportedEncodingException {
        String requestUrl = String.format("%s/%s", DRONE_API_BASE_URL, "availableForLoad");
        List<MedicationLoadingResponse> medicationLoadingResponseList = List.of(buildMedicationLoadingResponse());
        MedicationLoadingResponsePage page = new MedicationLoadingResponsePage();
        page.setPageNumber(0);
        page.setPageSize(10);
        page.setTotalElements(20);
        page.setMedicationLoadingResponseList(medicationLoadingResponseList);
        when(droneComponent.getDronesAvailableForLoading(any())).thenReturn(page);

        MockHttpServletResponse response = performGetRequest(requestUrl);
        String responseBody = response.getContentAsString();

        verify(droneComponent, times(1)).getDronesAvailableForLoading(any(MedicationLoadingRequest.class));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseBody.contains(medicationLoadingResponseList.get(0).getDroneModel()));
    }
}
