package com.comp2005.EmployeeAndPatients;

import com.comp2005.FastDischarge.FastDischargeApiService;
import com.comp2005.FastDischarge.FastDischargeController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

public class EmployeeAndPatientControllerUnitTest {
    @Mock
    private EmployeeAndPatientApiService apiService;

    private EmployeeAndPatientController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EmployeeAndPatientController(apiService);
    }
    @Test
    public void testEmployeeAndPatientEndPoint() throws JsonProcessingException {
        // Prepare
        int id = 4;
        List<EmployeeAndPatientResponse.Patient> mockPatients = new ArrayList<>();
        mockPatients.add(new EmployeeAndPatientResponse.Patient("Heather","Carter","222444666"));
        EmployeeAndPatientResponse employeeAndPatientResponse = new EmployeeAndPatientResponse(4,"Sarah","Jones", mockPatients);

        when(apiService.getPatientListResponse(id)).thenReturn(employeeAndPatientResponse);

        // Execute
        ResponseEntity<String> response = controller.employeeAndPatientEndPoint(id);

        // Verify
        verify(apiService).getPatientListResponse(id);
        // Additional assertions on the response if needed
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testExceptionHandling() throws JsonProcessingException {
        EmployeeAndPatientApiService apiService = Mockito.mock(EmployeeAndPatientApiService.class);

        EmployeeAndPatientController controller = new EmployeeAndPatientController(apiService);

        // Set up the mock service to throw an exception
        when(apiService.getPatientListResponse(4)).thenThrow(new RuntimeException("Test exception"));

        // Invoke the endpoint
        ResponseEntity<String> response = controller.employeeAndPatientEndPoint(4);

        // Assert the response
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_JSON);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals("{\"Result\":\"An unknown error occurred\"}", response.getBody());
    }
}
