package com.comp2005.FastDischarge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class FastDischargeResponseUnitTest {

    @Test
    void testGettersAndSetters() {
        // Create sample data
        FastDischargeRecord record1 = new FastDischargeRecord(1, "John", "Doe", "123456789", "2022-01-01", "2022-01-02", 24);
        FastDischargeRecord record2 = new FastDischargeRecord(2, "Jane", "Smith", "987654321", "2022-02-01", "2022-02-02", 24);
        ArrayList<FastDischargeRecord> fastDischarges = new ArrayList<>();
        fastDischarges.add(record1);
        fastDischarges.add(record2);

        // Create a FastDischargeResponse object
        FastDischargeResponse response = new FastDischargeResponse();
        response.setFastDischargeArray(fastDischarges);

        // Test the getters and setters
        Assertions.assertEquals(fastDischarges, response.getFastDischargeArray());
    }

    @Test
    void testToJsonString() throws JsonProcessingException {
        // Create sample data
        FastDischargeRecord record1 = new FastDischargeRecord(1, "John", "Doe", "123456789", "2022-01-01", "2022-01-02", 24);
        FastDischargeRecord record2 = new FastDischargeRecord(2, "Jane", "Smith", "987654321", "2022-02-01", "2022-02-02", 24);
        ArrayList<FastDischargeRecord> fastDischarges = new ArrayList<>();
        fastDischarges.add(record1);
        fastDischarges.add(record2);

        // Create a FastDischargeResponse object
        FastDischargeResponse response = new FastDischargeResponse();
        response.setFastDischargeArray(fastDischarges);

        // Convert the object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String actualJsonString = objectMapper.writeValueAsString(response);

        // Convert the expected JSON string to a JSON object
        JsonNode expectedJson = objectMapper.readTree("{\"faDischargeArray\":[{\"patientId\":1,\"forename\":\"John\",\"surname\":\"Doe\",\"nhsnumber\":\"123456789\",\"admissionDate\":\"2022-01-01\",\"dischargeDate\":\"2022-01-02\",\"durationHours\":24},{\"patientId\":2,\"forename\":\"Jane\",\"surname\":\"Smith\",\"nhsnumber\":\"987654321\",\"admissionDate\":\"2022-02-01\",\"dischargeDate\":\"2022-02-02\",\"durationHours\":24}]}");

        // Convert the actual JSON string to a JSON object
        JsonNode actualJson = objectMapper.readTree(actualJsonString);

        // Compare the JSON objects
        Assertions.assertEquals(expectedJson, actualJson);
    }

}
