package com.comp2005.BusiestDay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BusiestDayApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BusiestDayApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public BusiestDayApiService()
    {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

        public BusiestDayResponse getBusiestDayResponse() throws JsonProcessingException {
            String admissionUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";
            String admissionResponse = restTemplate.getForObject(admissionUrlBase, String.class);
            BusiestDayResponse response = new BusiestDayResponse();
            try {
                //ObjectMapper objectMapper = new ObjectMapper();
                JsonNode admissionArray = objectMapper.readTree(admissionResponse);
                System.out.println("Admission Response : " + admissionResponse);
                System.out.println("Admission Array : " + admissionArray);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                Map<DayOfWeek, Integer> dayCountMap = new HashMap<>();

                for (JsonNode admissionNode : admissionArray) {
                    // Get the date string from the JSON object
                    String dateString = admissionNode.get("admissionDate").asText();

                    // Parse the date string to LocalDate
                    LocalDate date = LocalDate.parse(dateString, dateFormatter);

                    // Get the day of the week from the parsed date
                    DayOfWeek dayOfWeek = date.getDayOfWeek();

                    // Update the count in the map
                    dayCountMap.put(dayOfWeek, dayCountMap.getOrDefault(dayOfWeek, 0) + 1);
                }

                List<String> mostFrequentDays = new ArrayList<>();
                int highestCount = 0;

                for (Map.Entry<DayOfWeek, Integer> entry : dayCountMap.entrySet()) {
                    String day = String.valueOf(entry.getKey());
                    int count = entry.getValue();

                    if (count > highestCount) {
                        highestCount = count;
                        mostFrequentDays.clear();
                        mostFrequentDays.add(day);
                    } else if (count == highestCount) {
                        mostFrequentDays.add(day);
                    }
                }
                Collections.sort(mostFrequentDays);

                response.setBusiestDay(mostFrequentDays.get(0));
                response.setAdmissions(highestCount);
                return response;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

