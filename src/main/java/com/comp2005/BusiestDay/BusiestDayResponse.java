package com.comp2005.BusiestDay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BusiestDayResponse {
        private String busiestDay;
        private int admissions;

    public BusiestDayResponse() {
    }

    public BusiestDayResponse(String busiestDay, int admissions) {
        this.busiestDay= busiestDay;
        this.admissions = admissions;
    }
    public String getBusiestDay() {
        return busiestDay;
    }
    public void setBusiestDay(String busiestDay) {
        this.busiestDay = busiestDay;
    }

    public int getAdmissions() {
        return admissions;
    }
    public void setAdmissions(int admissions) {
        this.admissions = admissions;
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
