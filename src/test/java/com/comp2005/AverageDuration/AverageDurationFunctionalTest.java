package com.comp2005.AverageDuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AverageDurationFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {

    }

    @Test
    void averageDuration_RequestOK() throws Exception {


        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/averageduration/4"))
                .andExpect(status().isOk());

    }

    @Test
    void averageDuration_ExpectedData() throws Exception {
        String expectedResponse = "{\"employeeId\":4,\"employeeForename\":\"Sarah\",\"employeeSurname\":\"Jones\",\"averageDuration\":\"1 Days, 21 Hours, 38 Minutes\"}";
        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/averageduration/4"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    void averageDuration_IsJSON() throws Exception {

        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/averageduration/4"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


}
