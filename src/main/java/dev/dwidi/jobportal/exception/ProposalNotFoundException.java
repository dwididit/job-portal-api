package dev.dwidi.jobportal.exception;

public class ProposalNotFoundException extends RuntimeException {
    public ProposalNotFoundException(String message) {
        super(message);
    }
}
