package com.comp2005.FastDischarge;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FastDischargeController {
    private final FastDischargeApiService apiService;

    public FastDischargeController(FastDischargeApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/api/comp2005/fastdischarge")
    @ResponseBody
    public ResponseEntity<String> fastDischargeEndPoint(){

        try {
            FastDischargeResponse response = apiService.getFastDischargeResponse();

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
