package com.comp2005.EmployeeAndPatients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class EmployeeAndPatientResponse {
    private int employeeId;
    private String employeeForename;
    private String employeeSurname;
    private List<Patient> patients;

    // Constructors
    public EmployeeAndPatientResponse() {
    }

    public EmployeeAndPatientResponse(int employeeId, String employeeForename, String employeeSurname, List<Patient> patients) {
        this.employeeId = employeeId;
        this.employeeForename = employeeForename;
        this.employeeSurname = employeeSurname;
        this.patients = patients;
    }

    // Getters and Setters

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
    public List<Patient> getPatients() {
        return patients;
    }
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }



    // Nested class
    static class Patient {
        private String patientForename;
        private String patientSurname;
        private String NHSNumber;

        public Patient(){

        }
        public Patient(String patientForename, String patientSurname, String NHSNumber) {
            this.patientForename = patientForename;
            this.patientSurname = patientSurname;
            this.NHSNumber = NHSNumber;
        }

        // Get and Set
        public String getPatientForename() {
            return patientForename;
        }

        public void setPatientForename(String patientForename) {
            this.patientForename = patientForename;
        }

        public String getPatientSurname() {
            return patientSurname;
        }

        public void setPatientSurname(String patientSurname) {
            this.patientSurname = patientSurname;
        }
        public String getNHSNumber() {
            return NHSNumber;
        }

        public void setNHSNumber(String NHSNumber) {
            this.NHSNumber = NHSNumber;
        }
    }
    // Convert the response object to a JSON string
    public String toJsonString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

}
