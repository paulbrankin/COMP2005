package com.comp2005.AverageDuration;

import com.comp2005.AverageDuration.AverageDurationApiService;
import com.comp2005.AverageDuration.AverageDurationController;
import com.comp2005.AverageDuration.AverageDurationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AverageDurationIntegrationTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private AverageDurationApiService apiService;
    private AverageDurationController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new AverageDurationApiService(restTemplate, objectMapper);
        controller = new AverageDurationController(apiService);
    }

    @Test
    public void testGetAverageDurationEndPointIntegration() throws JsonProcessingException {
        // Mock the REST API response
        String employeeResponse = "{\"id\":4,\"surname\":\"Jines\",\"forename\":\"Sarah\"}";
        String allocationResponse = "[{\"id\":99,\"admissionID\":1,\"employeeID\":4,\"startTime\":\"2020-11-28T16:45:00\",\"endTime\":\"2020-11-28T23:56:00\"}," +
                "{\"id\":2,\"admissionID\":3,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}," +
                "{\"id\":98,\"admissionID\":6,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}]";

        String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2}," +
                "{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}," +
                "{\"id\":4,\"admissionDate\":\"2023-05-04T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":5,\"admissionDate\":\"2023-04-27T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":6,\"admissionDate\":\"2023-03-30T22:14:00\",\"dischargeDate\":\"2023-04-03T00:00:00\",\"patientID\":1}," +
                "{\"id\":7,\"admissionDate\":\"2023-03-09T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}]";

        when(restTemplate.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/.*"), Mockito.any(), anyInt()))
                .thenReturn(employeeResponse);
        when(restTemplate.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations/.*"), Mockito.any()))
                .thenReturn(allocationResponse);
        when(restTemplate.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions/*"), Mockito.any()))
                .thenReturn(admissionResponse);

        // Mock the JSON parsing
        JsonNode employeeDetailNode = new ObjectMapper().readTree(employeeResponse);
        when(objectMapper.readTree(employeeResponse)).thenReturn(employeeDetailNode);
        JsonNode allocationNode = new ObjectMapper().readTree(allocationResponse);
        when(objectMapper.readTree(allocationResponse)).thenReturn(allocationNode);
        JsonNode admissionNode = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(admissionResponse)).thenReturn(admissionNode);

        // Execute
        AverageDurationResponse expectedResponse = new AverageDurationResponse();
        expectedResponse.setEmployeeId(4);
        expectedResponse.setAverageDuration("2 Days, 7 Hours, 1 Minutes");

        ResponseEntity<String> response = controller.averageDurationEndPoint(4);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.getBody().toString());

        // Additional assertions on the response if needed
        assertEquals(HttpStatus.OK, response.getStatusCode());









    }
}
