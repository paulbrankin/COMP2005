package com.comp2005.AverageDuration;

import com.comp2005.Exceptions.DataQualityIssue;
import com.comp2005.Exceptions.EmployeeHasNoAllocations;
import com.comp2005.Exceptions.EmployeeNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class AverageDurationApiServiceUnitTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private AverageDurationApiService averageDurationApiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        averageDurationApiService = new AverageDurationApiService(restTemplate, objectMapper);
    }

    String employeeResponse = "{\"id\":4,\"surname\":\"Jines\",\"forename\":\"Sarah\"}";
    String allocationResponse = "[{\"id\":99,\"admissionID\":1,\"employeeID\":4,\"startTime\":\"2020-11-28T16:45:00\",\"endTime\":\"2020-11-28T23:56:00\"}," +
            "{\"id\":2,\"admissionID\":3,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}," +
            "{\"id\":98,\"admissionID\":6,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}," +
            "{\"id\":97,\"admissionID\":4,\"employeeID\":4,\"startTime\":\"2020-05-23T21:50:00\",\"endTime\":\"2020-05-31T03:22:00\"}]";
    String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2}," +
            "{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
            "{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}," +
            "{\"id\":4,\"admissionDate\":\"2023-05-04T22:14:00\",\"dischargeDate\":\"2023-05-17T00:00:00\",\"patientID\":1}," +
            "{\"id\":5,\"admissionDate\":\"2023-04-27T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
            "{\"id\":6,\"admissionDate\":\"2023-03-30T22:14:00\",\"dischargeDate\":\"2023-04-03T00:00:00\",\"patientID\":1}," +
            "{\"id\":7,\"admissionDate\":\"2023-03-09T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}]";


    @Test
    public void testGetAverageDurationResponds() throws JsonProcessingException {
        // Mock the REST API response

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

        // Perform the test

        AverageDurationResponse actualResponse = averageDurationApiService.getAverageDurationResponse(4);

        // Verify the result
        assertEquals(4, actualResponse.getEmployeeId());
    }

    @Test
    public void testNegativeAverageDuration() throws JsonProcessingException {
        // Mock the REST API response
        String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2}," +
                "{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"0001-09-27T09:56:00\",\"patientID\":2}," +
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

        // Perform the test
        assertThrows(DataQualityIssue.class, () -> {
            averageDurationApiService.getAverageDurationResponse(4);
        });
    }


    @Test
    public void testNoAllocationsForEmpId() throws JsonProcessingException {
        // Mock the REST API response

        String allocationResponse = "";
        String admissionResponse = "";

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

        // Perform the test
        assertThrows(EmployeeHasNoAllocations.class, () -> {
            averageDurationApiService.getAverageDurationResponse(4);
        });
    }

    @Test
    public void testNegativeEmpId() {
        // Create an instance of AverageDurationApiService
        AverageDurationApiService averageDurationApiService = new AverageDurationApiService();

        // Define the invalid id
        int invalidId = -1;

        // Assert that the method throws an IllegalArgumentException with the expected error message
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            averageDurationApiService.getAverageDurationResponse(invalidId);
        }, "ID must be greater than or equal to zero");
    }

    @Test
    public void testNonExistingEmpID() throws JsonProcessingException {
        // Mock the REST API response

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

        // Perform the test
        int nonExistingEmpId = 90909090;

        // Verify the result
        assertThrows(EmployeeNotFoundException.class, () -> {
            averageDurationApiService.getAverageDurationResponse(nonExistingEmpId);
        });
    }

}
