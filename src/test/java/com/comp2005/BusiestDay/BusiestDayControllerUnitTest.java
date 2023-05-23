package com.comp2005.BusiestDay;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BusiestDayControllerUnitTest {
    @Mock
    private BusiestDayApiService apiService;

    private BusiestDayController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BusiestDayController(apiService);
    }
    @Test
    public void BusiestDayEndPoint() throws JsonProcessingException {
        // Prepare

        BusiestDayResponse busiestDayResponse = new BusiestDayResponse("Saturday",143);

        when(apiService.getBusiestDayResponse()).thenReturn(busiestDayResponse);

        // Execute
        ResponseEntity<String> response = controller.busiestDayEndPoint();

        // Verify
        verify(apiService).getBusiestDayResponse();
        assertEquals(200, response.getStatusCodeValue());

    }
    @Test
    public void testExceptionHandling() throws JsonProcessingException {
        BusiestDayApiService apiService = Mockito.mock(BusiestDayApiService.class);

        BusiestDayController controller = new BusiestDayController(apiService);

        // Set up the mock service to throw an exception
        when(apiService.getBusiestDayResponse()).thenThrow(new RuntimeException("Test exception"));

        // Invoke the endpoint
        ResponseEntity<String> response = controller.busiestDayEndPoint();

        // Assert the response
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_JSON);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals("{\"Result\":\"An unknown error occurred\"}", response.getBody());
    }
}
