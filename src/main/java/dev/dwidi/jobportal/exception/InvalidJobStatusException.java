package dev.dwidi.jobportal.exception;

public class InvalidJobStatusException extends RuntimeException {
    public InvalidJobStatusException(String message) {
        super(message);
    }
}
