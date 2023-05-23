package com.comp2005.BusiestDay;

import com.comp2005.BusiestDay.BusiestDayApiService;
import com.comp2005.BusiestDay.BusiestDayController;
import com.comp2005.BusiestDay.BusiestDayResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusiestDayIntegrationTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private BusiestDayApiService apiService;
    private BusiestDayController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new BusiestDayApiService(restTemplate, objectMapper);
        controller = new BusiestDayController(apiService);
    }
    final String admissionsResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2},{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1},{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}]";
    final String patientResponse = "{\"id\":2,\"surname\":\"Carter\",\"forename\":\"Heather\",\"nhsNumber\":\"2224446666\"}";

    @Test
    public void testGetBusiestDayResponse() throws JsonProcessingException {
        // Mock the REST API response
        String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2}," +
                "{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}," +
                "{\"id\":4,\"admissionDate\":\"2023-05-04T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":5,\"admissionDate\":\"2023-04-27T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":6,\"admissionDate\":\"2023-03-30T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}," +
                "{\"id\":7,\"admissionDate\":\"2023-03-09T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1}]";

        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(admissionResponse);

        // Mock the JSON parsing
        JsonNode admissionArray = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(Mockito.anyString())).thenReturn(admissionArray);

        // Execute
        BusiestDayResponse expectedResponse = new BusiestDayResponse();
        expectedResponse.setBusiestDay("THURSDAY");
        expectedResponse.setAdmissions(5);

        ResponseEntity<String> response = controller.busiestDayEndPoint();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.getBody().toString());

        // Verify
        verify(restTemplate).getForObject(anyString(), eq(String.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());









    }
}
