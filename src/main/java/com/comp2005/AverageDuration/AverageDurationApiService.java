package com.comp2005.AverageDuration;

import com.comp2005.Exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AverageDurationApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AverageDurationApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public AverageDurationApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AverageDurationResponse getAverageDurationResponse(int id) throws JsonProcessingException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer.");
        }
        String empUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/{empID}";
        String allocUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations/";
        String admissionUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions/";

        AverageDurationResponse response = new AverageDurationResponse();
        long averageDuration = 0;
        long totalDuration = 0;
        int admissionCount = 0;
        String employeeResponse = "";

        try {
            employeeResponse = restTemplate.getForObject(empUrlBase, String.class, id);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new EmployeeNotFoundException("Employee not found for ID: " + id);
        }

        JsonNode employeeDetailNode = objectMapper.readTree(employeeResponse);
        if (employeeDetailNode.get("id").asInt() != id || employeeResponse.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for ID: " + id);
        }
        response.setEmployeeId(employeeDetailNode.get("id").asInt());
        response.setEmployeeForename(employeeDetailNode.get("forename").asText());
        response.setEmployeeSurname(employeeDetailNode.get("surname").asText());

        String allocationResponse = restTemplate.getForObject(allocUrlBase, String.class);
        JsonNode allocationNode = objectMapper.readTree(allocationResponse);

        for (JsonNode allocation : allocationNode) {
            if (allocation.get("employeeID").asInt() == response.getEmployeeId()) {
                String admissionResponse = restTemplate.getForObject(admissionUrlBase, String.class);
                JsonNode admissionNode = objectMapper.readTree(admissionResponse);
                for (JsonNode admission : admissionNode) {
                    if (admission.get("id") == allocation.get("admissionID")) {


                        long minutesDuration = Duration.between(LocalDateTime.parse(admission.get("admissionDate").asText()),
                                LocalDateTime.parse(admission.get("dischargeDate").asText())).toMinutes();
                        totalDuration += minutesDuration;
                        admissionCount += 1;
                    }
                }
            }
        }
        if (admissionCount > 0 && totalDuration>0) {
            Duration duration = Duration.ofMinutes(totalDuration/admissionCount);
            long days = duration.toDays();
            long hours = duration.toHours() % 24;
            long remainingMinutes = duration.toMinutes() % 60;
            response.setAverageDuration(days+ " Days, "+hours+" Hours, " +remainingMinutes+ " Minutes");
        } else
         if(admissionCount <1) {
            throw new EmployeeHasNoAllocations("Employee : " + id + " has no allocations/admissions.");
        } else
            if(totalDuration<1) {
                throw new DataQualityIssue("Employee : " +id + " has data quality issues with admissions");
            }

        try {
            String jsonResponse = objectMapper.writeValueAsString(response);
            String unescapedJsonResponse = StringEscapeUtils.unescapeJson(jsonResponse);
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}