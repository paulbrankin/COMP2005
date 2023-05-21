package com.comp2005.EmployeeAndPatients;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        int employeeId = 4;
        List<EmployeeAndPatientResponse.Patient> mockPatients = new ArrayList<>();
        mockPatients.add(new EmployeeAndPatientResponse.Patient("Heather","Carter","222444666"));
        EmployeeAndPatientResponse employeeAndPatientResponse = new EmployeeAndPatientResponse(4,"Sarah","Jones", mockPatients);

        when(apiService.getPatientListResponse(employeeId)).thenReturn(employeeAndPatientResponse);

        // Execute
        ResponseEntity<String> response = controller.employeeAndPatientEndPoint(employeeId);

        // Verify
        verify(apiService).getPatientListResponse(employeeId);
        // Additional assertions on the response if needed
        assertEquals(200, response.getStatusCodeValue());

    }
}
