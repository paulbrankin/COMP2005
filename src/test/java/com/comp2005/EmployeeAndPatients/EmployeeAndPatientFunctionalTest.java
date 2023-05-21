package com.comp2005.EmployeeAndPatients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmployeeAndPatientFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setup() {
        // Set up any required test data or dependencies
    }

    @Test
    void employeeAndPatient_RequestOK() throws Exception {

        String expectedResponse = "{\"employeeId\":4,\"employeeForename\":\"Sarah\",\"employeeSurname\":\"Jones\",\"patients\":[{\"patientForename\":\"Heather\",\"patientSurname\":\"Carter\",\"nhsnumber\":\"2224446666\"}]}";

        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", 4))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }
    @Test
    void employeeAndPatient_ExpectedData() throws Exception {
        String expectedResponse = "{\"employeeId\":4,\"employeeForename\":\"Sarah\",\"employeeSurname\":\"Jones\",\"patients\":[{\"patientForename\":\"Heather\",\"patientSurname\":\"Carter\",\"nhsnumber\":\"2224446666\"}]}";

        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", 4))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }
    @Test
    void employeeAndPatient_IsJSON() throws Exception {
        String expectedResponse = "{\"employeeId\":4,\"employeeForename\":\"Sarah\",\"employeeSurname\":\"Jones\",\"patients\":[{\"patientForename\":\"Heather\",\"patientSurname\":\"Carter\",\"nhsnumber\":\"2224446666\"}]}";

        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", 4))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void employeeAndPatient_NoDuplicatePatients() throws Exception {

        String expectedResponse = "{\"employeeId\":4,\"employeeForename\":\"Sarah\",\"employeeSurname\":\"Jones\",\"patients\":[{\"patientForename\":\"Heather\",\"patientSurname\":\"Carter\",\"nhsnumber\":\"2224446666\"}]}";

        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patients", hasSize(1)));
    }
    @Test
    void employeeAndPatient_NoAdmissionsForID() throws Exception {

        mockMvc.perform(get("/api/comp2005/patientlist/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"employeeId\":3,\"employeeForename\":\"Alice\",\"employeeSurname\":\"Allen\",\"patients\":null}"));
    }

    @Test
    void employeeAndPatient_NegativeEmpID() throws Exception {
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void employeeAndPatient_AplhaEmpID() throws Exception {
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", "A")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void employeeAndPatient_NoEmpId() throws Exception {
        mockMvc.perform(get("/api/comp2005/patientlist/{id}", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
