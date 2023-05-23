package com.comp2005.FastDischarge;

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
class FastDischargeFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
    }

    @Test
    void fastDischarge_RequestOK() throws Exception {


        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/fastdischarge"))
                .andExpect(status().isOk());

    }

    @Test
    void fastDischarge_ExpectedData() throws Exception {
        String expectedResponse = "{\"fastDischargeArray\":[{\"patientId\":2,\"forename\":\"Heather\",\"surname\":\"Carter\",\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"durationHours\":7,\"nhsnumber\":\"2224446666\"}]}";

        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/fastdischarge"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    void fastDischarge_IsJSON() throws Exception {

        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/fastdischarge"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


}
