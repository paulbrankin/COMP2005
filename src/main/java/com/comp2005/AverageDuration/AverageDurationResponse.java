package com.comp2005.AverageDuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public  class  AverageDurationResponse {
        private int employeeId;
        private String employeeForename;
        private String employeeSurname;
        private String averageDuration;

        // Constructors
        public AverageDurationResponse(){

        }
        public AverageDurationResponse(int employeeId, String employeeForename, String employeeSurname, String averageDuration) {
            this.employeeId = employeeId;
            this.employeeForename = employeeForename;
            this.employeeSurname = employeeSurname;
            this.averageDuration = averageDuration;
        }

        // Get and Set
        public int getEmployeeId() {
            return employeeId;
        }
        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }
        public String getEmployeeForename() {
            return employeeForename;
        }
        public void setEmployeeForename(String employeeForename) {
            this.employeeForename = employeeForename;
        }
        public String getEmployeeSurname() {
            return employeeSurname;
        }
        public void setEmployeeSurname(String employeeSurname) {
            this.employeeSurname = employeeSurname;
        }
        public String getAverageDuration() {
            return averageDuration;
        }
        public void setAverageDuration(String averageDuration) {
            this.averageDuration = averageDuration;
        }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
    }


