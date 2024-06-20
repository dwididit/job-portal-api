package dev.dwidi.jobportal.exception;

public class EmployerNotFoundException extends RuntimeException {
    public EmployerNotFoundException(String employerId) {
        super(String.valueOf(employerId));
    }
}
