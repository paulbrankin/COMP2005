package com.comp2005.AverageDuration;

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

public class AverageDurationControllerUnitTest {
    @Mock
    private AverageDurationApiService apiService;

    private AverageDurationController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AverageDurationController(apiService);
    }
    @Test
    public void testAverageDurationEndPoint() throws JsonProcessingException {
        // Prepare

        AverageDurationResponse averageDurationResponse = new AverageDurationResponse(4, "Alex", "Brankin", "4 Days, 3 Hours, 2 Minutes");
        when(apiService.getAverageDurationResponse(4)).thenReturn(averageDurationResponse);

        // Execute
        ResponseEntity<String> response = controller.averageDurationEndPoint(4);

        // Verify
        verify(apiService).getAverageDurationResponse(4);
        // Additional assertions on the response if needed
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testExceptionHandling() throws JsonProcessingException {
            AverageDurationApiService apiServic = Mockito.mock(AverageDurationApiService.class);

            AverageDurationController controller = new AverageDurationController(apiService);

            // Set up the mock service to throw an exception
            int testId = 888;
            when(apiService.getAverageDurationResponse(testId)).thenThrow(new RuntimeException("Test exception"));

            // Invoke the endpoint
            ResponseEntity<String> response = controller.averageDurationEndPoint(testId);

            // Assert the response
            HttpHeaders expectedHeaders = new HttpHeaders();
            expectedHeaders.setContentType(MediaType.APPLICATION_JSON);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(expectedHeaders, response.getHeaders());
            assertEquals("Test exception", response.getBody());
        }
    }


