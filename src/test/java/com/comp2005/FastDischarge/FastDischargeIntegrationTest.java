package com.comp2005.FastDischarge;

import com.comp2005.FastDischarge.FastDischargeApiService;
import com.comp2005.FastDischarge.FastDischargeController;
import com.comp2005.FastDischarge.FastDischargeRecord;
import com.comp2005.FastDischarge.FastDischargeResponse;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FastDischargeIntegrationTest {
    @Mock
    private RestTemplate restTemplateMock;
@Mock
    private ObjectMapper objectMapper;
    private FastDischargeApiService apiService;
    private FastDischargeController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new FastDischargeApiService(restTemplateMock,objectMapper);
        controller = new FastDischargeController(apiService);
    }
    final String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2},{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1},{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}]";
    final String patientResponse = "{\"id\":2,\"surname\":\"Carter\",\"forename\":\"Heather\",\"nhsNumber\":\"2224446666\"}";

    @Test
    public void testFastDischargeEndPointIntegration() throws JsonProcessingException {
        // Prepare

        // Set up the behavior of the mocked RestTemplate
        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Patients.*"), Mockito.any(), anyInt()))
                .thenReturn(patientResponse);

        when(restTemplateMock.getForObject(Mockito.matches("https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions.*"), Mockito.any()))
                .thenReturn(admissionResponse);

        JsonNode patientNode = new ObjectMapper().readTree(patientResponse);
        when(objectMapper.readTree(patientResponse)).thenReturn(patientNode);
        JsonNode admissionNode = new ObjectMapper().readTree(admissionResponse);
        when(objectMapper.readTree(admissionResponse)).thenReturn(admissionNode);

        FastDischargeResponse mockFastDischargeResponse = new FastDischargeResponse();
        ArrayList<FastDischargeRecord> mockDischarges = new ArrayList<>();
        FastDischargeRecord mockDischarge1 = new FastDischargeRecord(88,"Tom","Berringer","66277227","2023-05-10T12:00:00","2023-05-11T12:00:00",24 );
        mockDischarges.add(mockDischarge1);
        mockFastDischargeResponse.setFastDischargeArray(mockDischarges);

        // Execute
        ResponseEntity<String> response = controller.fastDischargeEndPoint();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.getBody().toString());

        // Verify
        verify(restTemplateMock).getForObject(anyString(), eq(String.class));
        // Additional assertions on the response if needed
        assertEquals(HttpStatus.OK, response.getStatusCode());

        System.out.println(responseBody.get("fastDischargeArray"));








    }
}
