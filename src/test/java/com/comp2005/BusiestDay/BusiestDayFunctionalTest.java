package com.comp2005.BusiestDay;

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
class BusiestDayFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Set up any required test data or dependencies
    }

    @Test
    void busiestDay_RequestOK() throws Exception {


        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/busiestday"))
                .andExpect(status().isOk());

    }

    @Test
    void busiestDay_ExpectedData() throws Exception {
        String expectedResponse = "{\"busiestDay\":\"MONDAY\",\"admissions\":1}";
        System.out.println(("expectedResponse : " + expectedResponse));
        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/busiestday"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    void busiestDay_IsJSON() throws Exception {

        // Invoke the method under test using MockMvc
        mockMvc.perform(get("/api/comp2005/busiestday"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


}
