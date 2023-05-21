package com.comp2005.FastDischarge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.Math.round;

@Service
public class FastDischargeApiService {
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public FastDischargeApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public FastDischargeApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public FastDischargeResponse getFastDischargeResponse() throws JsonProcessingException {
        String admissionUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";
        String patientUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Patients/{patientID}";

        String admissionResponse = restTemplate.getForObject(admissionUrlBase, String.class);
        System.out.println("Admissions : "+ admissionResponse);

            FastDischargeResponse response = new FastDischargeResponse();
            JsonNode admissionArray = objectMapper.readTree(admissionResponse);
            ArrayList<FastDischargeRecord> fastDischargeRecordList = new ArrayList<>();
            for (JsonNode admissionNode : admissionArray) {
                System.out.println("Date Time : " + LocalDateTime.parse(admissionNode.get("dischargeDate").asText()));
                System.out.println("Duration : " + Duration.between(LocalDateTime.parse(admissionNode.get("admissionDate").asText()),
                        LocalDateTime.parse(admissionNode.get("dischargeDate").asText())).toMinutes());
                long minutesDuration = Duration.between(LocalDateTime.parse(admissionNode.get("admissionDate").asText()),
                        LocalDateTime.parse(admissionNode.get("dischargeDate").asText())).toMinutes();
                if (minutesDuration > 0 && minutesDuration <= 4320) {
                    int patId = admissionNode.get("patientID").asInt();
                    //String patientResponse = restTemplate.getForObject(patientUrlBase, String.class, admissionNode.get("patientID"));
                    String patientResponse = restTemplate.getForObject(patientUrlBase, String.class, patId);
                    JsonNode patientNode = objectMapper.readTree(patientResponse);
                    System.out.println("Patient : " + patientResponse);
                    FastDischargeResponse fred = new FastDischargeResponse();

                    fastDischargeRecordList.add(new FastDischargeRecord(admissionNode.get("patientID").asInt(),
                            patientNode.get("forename").asText(),
                            patientNode.get("surname").asText(),
                            patientNode.get("nhsNumber").asText(),
                            admissionNode.get("admissionDate").asText(),
                            admissionNode.get("dischargeDate").asText(),
                            round((float) minutesDuration / 60)));
                }
            }
            response.setFastDischargeArray(fastDischargeRecordList);



            try {
                String jsonResponse = objectMapper.writeValueAsString(response);
                String unescapedJsonResponse = StringEscapeUtils.unescapeJson(jsonResponse);
                return response;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}