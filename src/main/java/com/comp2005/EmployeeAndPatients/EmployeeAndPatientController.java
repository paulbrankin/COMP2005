package com.comp2005.EmployeeAndPatients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeAndPatientController {
    private final EmployeeAndPatientApiService apiService;

    public EmployeeAndPatientController(EmployeeAndPatientApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/api/comp2005/patientlist/{id}")
    @ResponseBody
    public ResponseEntity<String> employeeAndPatientEndPoint(@PathVariable int id) {

        try {
            EmployeeAndPatientResponse response = apiService.getPatientListResponse(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.toJsonString());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<String> handleException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body("{\"Result\":\"An unknown error occurred\"}");
    }
}
