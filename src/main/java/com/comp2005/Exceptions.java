package com.comp2005;

public class Exceptions {

    public static class DataQualityIssue extends RuntimeException {
        public DataQualityIssue(String message) {
            super(message);
        }
    }

    public static class EmployeeHasNoAllocations extends RuntimeException {
        public EmployeeHasNoAllocations(String message) {
            super(message);
        }
    }

    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(String message) {
            super(message);
        }
    }
}
