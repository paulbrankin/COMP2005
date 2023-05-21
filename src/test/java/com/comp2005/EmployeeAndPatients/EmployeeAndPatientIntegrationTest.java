package com.comp2005.EmployeeAndPatients;

import com.comp2005.EmployeeAndPatients.EmployeeAndPatientApiService;
import com.comp2005.EmployeeAndPatients.EmployeeAndPatientController;
import com.comp2005.EmployeeAndPatients.EmployeeAndPatientResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeAndPatientIntegrationTest {
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ObjectMapper objectMapper;
    private EmployeeAndPatientApiService apiService;
    private EmployeeAndPatientController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new EmployeeAndPatientApiService(restTemplateMock, objectMapper);
        controller = new EmployeeAndPatientController(apiService);
    }
    final String employeeResponse = "{\"id\":4,\"surname\":\"Jones\",\"forename\":\"Sarah\"}";
    final String admissionResponse = "[{\"id\":1,\"admissionID\":1,\"employeeID\":4,\"startTime\":\"2029-11-28T16:45:00\",\"endTime\":\"2020-11-28T23:56:00\"},{\"id\":2,\"admissionID\":3,\"employeeID\":4,\"startTime\":\"2021-09-23T21:50:00\",\"endTime\":\"2021-09-24T09:50:00\"}]";
    final String patientDetails = "{\"id\":2,\"surname\":\"Cyrter\",\"forename\":\"Heather\",\"nhsNumber\":\"999999999\"}";
    final String patientResponse = "{\"id\":3,\"admissionDate\":\"2029-09-23T21:50:00\",\"dischargeDate\":\"2029-09-27T09:56:00\",\"patientID\":2}";

    @Test
    public void testEmployeeAndPatientEndPointIntegration() throws JsonProcessingException {
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

        List<EmployeeAndPatientResponse.Patient> mockPatients = new ArrayList<>();
        mockPatients.add(new EmployeeAndPatientResponse.Patient("Heather","Carter","222444666"));
        EmployeeAndPatientResponse employeeAndPatientResponse = new EmployeeAndPatientResponse(4,"Sarah","Jones", mockPatients);

        // Execute
        ResponseEntity<String> response = controller.employeeAndPatientEndPoint(employeeId);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.getBody().toString());

        // Verify
        verify(restTemplateMock).getForObject(anyString(), eq(String.class), eq(employeeId));
        //verify(objectMapper).readValue(eq(employeeResponse), eq(EmployeeDetails.class));
        // Additional assertions on the response if needed
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sarah", responseBody.get("employeeForename").asText());
        assertEquals("Sarah", responseBody.get("employeeForename").asText());




    }
}
