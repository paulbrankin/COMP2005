package com.comp2005.AverageDuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AverageDurationController {
    private final AverageDurationApiService apiService;

    public AverageDurationController(AverageDurationApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/api/comp2005/averageduration/{id}")
    @ResponseBody
    public ResponseEntity<String> averageDurationEndPoint(@PathVariable int id){

        try {
            AverageDurationResponse response = apiService.getAverageDurationResponse(id);

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
                .body(ex.getMessage());
    }
}
