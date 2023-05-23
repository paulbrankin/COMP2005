package com.comp2005.EmployeeAndPatients;

import com.comp2005.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeAndPatientApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EmployeeAndPatientApiService(RestTemplate restTemplate, ObjectMapper objectMapper)
    {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public EmployeeAndPatientApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public EmployeeAndPatientResponse getPatientListResponse(int employeeId) throws JsonProcessingException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer.");
        }
        String empUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/{empID}";
        String allocUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations/";
        String admissionUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions/{admissionID}";
        String patientUrlBase = "https://web.socem.plymouth.ac.uk/COMP2005/api/Patients/{patientID}";

        String admissionResponse = restTemplate.getForObject(allocUrlBase, String.class);

        EmployeeAndPatientResponse response = new EmployeeAndPatientResponse();

        String employeeResponse = restTemplate.getForObject(empUrlBase, String.class, employeeId);
        if (employeeResponse == null)
        {
            throw new Exceptions.EmployeeNotFoundException("Employee " + employeeId + " does not exist");
        }
        JsonNode employeeDetailNode = objectMapper.readTree(employeeResponse);
        response.setEmployeeId(employeeDetailNode.get("id").asInt());
        response.setEmployeeForename(employeeDetailNode.get("forename").asText());
        response.setEmployeeSurname(employeeDetailNode.get("surname").asText());

        JsonNode admissionArray = objectMapper.readTree(admissionResponse);
        List<EmployeeAndPatientResponse.Patient> patientList = new ArrayList<>();
        Set<String> uniquePatients = new HashSet<>();

        for (JsonNode admissionNode : admissionArray) {
            if (admissionNode.get("employeeID").asInt() == employeeId) {
                String patientResponse = restTemplate.getForObject(admissionUrlBase, String.class, admissionNode.get("admissionID").asInt());
                JsonNode patientNode = objectMapper.readTree(patientResponse);
                int patientId = patientNode.get("patientID").asInt();

                String patientDetails = restTemplate.getForObject(patientUrlBase, String.class, patientId);
                if (patientDetails.isBlank()){
                    throw new Exceptions.DataQualityIssue("No patients found for this employee");
                }
                JsonNode patientDetailsNode = objectMapper.readTree(patientDetails);

                if (uniquePatients.add(patientDetailsNode.get("nhsNumber").asText())) {
                    patientList.add(new EmployeeAndPatientResponse.Patient(
                            patientDetailsNode.get("forename").asText(),
                            patientDetailsNode.get("surname").asText(),
                            patientDetailsNode.get("nhsNumber").asText()
                    ));
                    response.setPatients(patientList);
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
        return response;
    }
}
