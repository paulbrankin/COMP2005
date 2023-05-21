package com.comp2005.FastDischarge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FastDischargeApiServiceUnitTest {
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ObjectMapper objectMapper;
    private FastDischargeApiService apiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService = new FastDischargeApiService(restTemplateMock, objectMapper);
    }

    final String admissionResponse = "[{\"id\":1,\"admissionDate\":\"2020-11-28T16:45:00\",\"dischargeDate\":\"2020-11-28T23:56:00\",\"patientID\":2},{\"id\":2,\"admissionDate\":\"2020-12-07T22:14:00\",\"dischargeDate\":\"0001-01-01T00:00:00\",\"patientID\":1},{\"id\":3,\"admissionDate\":\"2021-09-23T21:50:00\",\"dischargeDate\":\"2021-09-27T09:56:00\",\"patientID\":2}]";
    final String patientResponse = "{\"id\":2,\"surname\":\"Carter\",\"forename\":\"Heather\",\"nhsNumber\":\"2224446666\"}";

    @Test
    public void testGetFastDischarges() throws JsonProcessingException {
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

        // Execute
        FastDischargeResponse fastDischargeResponse = apiService.getFastDischargeResponse();

        // Verify
        verify(restTemplateMock).getForObject(anyString(), eq(String.class));
        assertEquals(1, fastDischargeResponse.getFastDischargeArray().size());
    }
}
