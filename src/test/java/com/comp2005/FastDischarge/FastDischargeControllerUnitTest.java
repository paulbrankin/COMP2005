package com.comp2005.FastDischarge;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FastDischargeControllerUnitTest {
    @Mock
    private FastDischargeApiService apiService;

    private FastDischargeController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new FastDischargeController(apiService);
    }
    @Test
    public void testFastDischargeEndPoint() throws JsonProcessingException {
        // Prepare

        FastDischargeResponse mockFastDischargeResponse = new FastDischargeResponse();
        ArrayList<FastDischargeRecord> mockDischarges = new ArrayList<>();
        FastDischargeRecord mockDischarge1 = new FastDischargeRecord(88,"Tom","Berringer","66277227","2023-05-10T12:00:00","2023-05-11T12:00:00",24 );
        mockDischarges.add(mockDischarge1);
        FastDischargeRecord mockDischarge2 = new FastDischargeRecord(89,"Dan","Spludge","6928282","2023-02-08T12:00:00","2023-02-10T12:00:00",24 );
        mockDischarges.add(mockDischarge2);

        mockFastDischargeResponse.setFastDischargeArray(mockDischarges);

        when(apiService.getFastDischargeResponse()).thenReturn(mockFastDischargeResponse);
        // Execute
        ResponseEntity<String> response = controller.fastDischargeEndPoint();

        // Verify
        verify(apiService).getFastDischargeResponse();
        // Additional assertions on the response if needed
        assertEquals(200, response.getStatusCodeValue());

    }
    @Test
    public void testExceptionHandling() throws JsonProcessingException {
        FastDischargeApiService apiService = Mockito.mock(FastDischargeApiService.class);

        FastDischargeController controller = new FastDischargeController(apiService);

        // Set up the mock service to throw an exception
        when(apiService.getFastDischargeResponse()).thenThrow(new RuntimeException("Test exception"));

        // Invoke the endpoint
        ResponseEntity<String> response = controller.fastDischargeEndPoint();

        // Assert the response
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_JSON);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals("{\"Result\":\"An unknown error occurred\"}", response.getBody());
    }
}
