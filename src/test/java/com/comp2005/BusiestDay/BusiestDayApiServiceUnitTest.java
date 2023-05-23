package com.comp2005.BusiestDay;

import com.comp2005.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BusiestDayApiServiceUnitTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private BusiestDayApiService busiestDayApiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        busiestDayApiService = new BusiestDayApiService(restTemplate, objectMapper);
    }

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

        // Perform the test
        BusiestDayResponse expectedResponse = new BusiestDayResponse();
        expectedResponse.setBusiestDay("THURSDAY");
        expectedResponse.setAdmissions(5);

        BusiestDayResponse actualResponse = busiestDayApiService.getBusiestDayResponse();

        // Verify the result
        assertEquals(expectedResponse.getBusiestDay(), actualResponse.getBusiestDay());
        assertEquals(expectedResponse.getAdmissions(), actualResponse.getAdmissions());
    }
    @Test
    public void testEmptyBusiestDayResponse() throws JsonProcessingException {
        // Mock the REST API response
        String admissionResponse = "";
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(admissionResponse);

        // Mock the JSON parsing
        JsonNode admissionArray = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(Mockito.anyString())).thenReturn(admissionArray);

        // Perform the test & verify result
        assertThrows(Exceptions.DataQualityIssue.class, () -> {
            busiestDayApiService.getBusiestDayResponse();
        });
    }

}
