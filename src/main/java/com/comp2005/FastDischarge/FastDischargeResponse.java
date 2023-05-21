package com.comp2005.FastDischarge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

class FastDischargeResponse {
    private ArrayList<FastDischargeRecord> fastDischargeArray;

    public FastDischargeResponse() {
        fastDischargeArray = new FastDischargeArray().getFastDischarges();
    }

    public ArrayList<FastDischargeRecord> getFastDischargeArray() {
        return fastDischargeArray;
    }

    public void setFastDischargeArray(ArrayList<FastDischargeRecord> fastDischargeArray) {
        this.fastDischargeArray = fastDischargeArray;
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    static class FastDischargeArray {
        public ArrayList<FastDischargeRecord> fastDischarges;

        public ArrayList<FastDischargeRecord> getFastDischarges() {
            return fastDischarges;
        }

        public void setFastDischarges(ArrayList<FastDischargeRecord> fastDischarges) {
            this.fastDischarges = fastDischarges;
        }
    }
}

class FastDischargeRecord {
    private int patientId;
    private String forename;
    private String surname;
    private String NHSNumber;
    private String admissionDate;
    private String dischargeDate;
    private long durationHours;

    public FastDischargeRecord(int patientId, String forename, String surname, String NHSNumber, String admissionDate, String dischargeDate, long durationHours) {
        this.patientId = patientId;
        this.forename = forename;
        this.surname = surname;
        this.NHSNumber = NHSNumber;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.durationHours = durationHours;
    }

    public FastDischargeRecord() {

    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNHSNumber() {
        return NHSNumber;
    }

    public void setNHSNumber(String NHSNumber) {
        this.NHSNumber = NHSNumber;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(long durationHours) {
        this.durationHours = durationHours;
    }
}
