package com.comp2005.EmployeeAndPatients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeAndPatientResponseUnitTest {

    @Test
    public void testGettersAndSetters() {

        EmployeeAndPatientResponse response = new EmployeeAndPatientResponse();
        List<EmployeeAndPatientResponse.Patient> patients = new ArrayList<>();
        EmployeeAndPatientResponse.Patient p1 = new EmployeeAndPatientResponse.Patient();
        p1.setPatientForename("Karl");
        p1.setPatientSurname("Smith");
        p1.setNHSNumber("3725252");
        patients.add(p1);
        response.setPatients(patients);
        response.setEmployeeId(888);
        response.setEmployeeForename("Alex");
        response.setEmployeeSurname("Brankin");
        response.setPatients(patients);

        // Get values using getters and assert
        assertEquals(888, response.getEmployeeId());
        assertEquals("Alex", response.getEmployeeForename());
        assertEquals("Brankin", response.getEmployeeSurname());

    }

    @Test
    public void testToJsonString() throws JsonProcessingException {
        // Create a test response object
        int employeeId = 1;
        String employeeForename = "Alex";
        String employeeSurname = "Brankin";
        List<EmployeeAndPatientResponse.Patient> patients = Arrays.asList(
                new EmployeeAndPatientResponse.Patient("Karl", "Smith", "123456"),
                new EmployeeAndPatientResponse.Patient("Danny", "Johnson", "789012")
        );
        EmployeeAndPatientResponse response = new EmployeeAndPatientResponse(employeeId, employeeForename, employeeSurname, patients);

        // Convert the response object to JSON string
        String jsonString = response.toJsonString();

        // Create an ObjectMapper to compare the JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = "{\"employeeId\":1,\"employeeForename\":\"Alex\",\"employeeSurname\":\"Brankin\",\"patients\":[{\"patientForename\":\"Karl\",\"patientSurname\":\"Smith\",\"nhsnumber\":\"123456\"},{\"patientForename\":\"Danny\",\"patientSurname\":\"Johnson\",\"nhsnumber\":\"789012\"}]}";

        // Assert the JSON string
        assertEquals(expectedJson, jsonString);
    }
}
