
package com.comp2005.EmployeeAndPatients;

import com.comp2005.AverageDuration.AverageDurationApiService;
import com.comp2005.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

public class EmployeeAndPatientsApiServiceUnitTest {
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ObjectMapper objectMapper;
    private EmployeeAndPatientApiService apiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new EmployeeAndPatientApiService(restTemplateMock, objectMapper);
    }

    final String employeeResponse = "{\"id\":4,\"surname\":\"Jones\",\"forename\":\"Sarah\"}";
    final String admissionResponse = "[{\"id\":1,\"admissionID\":1,\"employeeID\":4,\"startTime\":\"2029-11-28T16:45:00\",\"endTime\":\"2020-11-28T23:56:00\"},{\"id\":2,\"admissionID\":3,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}]";
    final String patientDetails = "{\"id\":2,\"surname\":\"Cyrter\",\"forename\":\"Heather\",\"nhsNumber\":\"999999999\"}";
    final String patientResponse = "{\"id\":3,\"admissionDate\":\"2029-09-23T21:50:00\",\"dischargeDate\":\"2029-09-27T09:56:00\",\"patientID\":2}";


    @Test
    public void testGetEmployeeDetails() throws JsonProcessingException {
        // Prepare
        int employeeId = 4;

        // Set up the behavior of the mocked RestTemplate
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/*"), Mockito.any(), anyInt()))
                .thenReturn(employeeResponse);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations/*"), Mockito.any()))
                .thenReturn(admissionResponse);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Patients/*"), Mockito.any(), anyInt()))
                .thenReturn(patientDetails);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions/*"), Mockito.any(), anyInt()))
                .thenReturn(patientResponse);

        // Mock the JSON parsing
        JsonNode employeeNode = new ObjectMapper().readTree(employeeResponse);
        when(objectMapper.readTree(employeeResponse)).thenReturn(employeeNode);
        JsonNode patientNode = new ObjectMapper().readTree(patientResponse);
        when(objectMapper.readTree(patientResponse)).thenReturn(patientNode);
        JsonNode admissionNode = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(admissionResponse)).thenReturn(admissionNode);
        JsonNode patientDetailsNode = new ObjectMapper().readTree(patientDetails);
        when(objectMapper.readTree(patientDetails)).thenReturn(patientDetailsNode);

        // Execute
        EmployeeAndPatientResponse employeeDetails = apiService.getPatientListResponse(employeeId);

        // Verify
        verify(restTemplateMock).getForObject(anyString(), eq(String.class), eq(employeeId));
        assertEquals(4, employeeDetails.getEmployeeId());
        assertEquals("Sarah", employeeDetails.getEmployeeForename());
        assertEquals("Jones", employeeDetails.getEmployeeSurname());
    }

    @Test
    public void testNegativeEmpId() throws JsonProcessingException {
        int employeeId = -1;

        // Assert that the method throws an IllegalArgumentException with the expected error message
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            apiService.getPatientListResponse(employeeId);
        }, "ID must be greater than or equal to zero");
    }
    @Test
    public void nonExistingEmpId() throws JsonProcessingException {
        int employeeId = 90909090;

        // Assert that the method throws an IllegalArgumentException with the expected error message
        Assertions.assertThrows(Exceptions.EmployeeNotFoundException.class, () -> {
            apiService.getPatientListResponse(employeeId);
        }, "ID must be greater than or equal to zero");
    }
    @Test
    public void testMissingPatientData() throws JsonProcessingException {
        int employeeId = 4;

        final String patientDetails = "";

        // Set up the behavior of the mocked RestTemplate
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/*"), Mockito.any(), anyInt()))
                .thenReturn(employeeResponse);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations/*"), Mockito.any()))
                .thenReturn(admissionResponse);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Patients/*"), Mockito.any(), anyInt()))
                .thenReturn(patientDetails);
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions/*"), Mockito.any(), anyInt()))
                .thenReturn(patientResponse);

        // Mock the JSON parsing
        JsonNode employeeNode = new ObjectMapper().readTree(employeeResponse);
        when(objectMapper.readTree(employeeResponse)).thenReturn(employeeNode);
        JsonNode patientNode = new ObjectMapper().readTree(patientResponse);
        when(objectMapper.readTree(patientResponse)).thenReturn(patientNode);
        JsonNode admissionNode = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(admissionResponse)).thenReturn(admissionNode);

        // Assert that the method throws an IllegalArgumentException with the expected error message
        Assertions.assertThrows(Exceptions.DataQualityIssue.class, () -> {
            apiService.getPatientListResponse(employeeId);
        }, "No patients found for this employee");
    }


}
