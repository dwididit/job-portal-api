package dev.dwidi.jobportal.exception;

public class ProposalAlreadyExistsException extends RuntimeException {
    public ProposalAlreadyExistsException(String message) {
        super(message);
    }
}
