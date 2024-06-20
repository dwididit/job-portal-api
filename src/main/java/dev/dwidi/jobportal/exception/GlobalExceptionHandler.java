package dev.dwidi.jobportal.exception;

import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PublicResponseDTO<String>> handleGlobalException(Exception e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("An error occurred here: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                requestId,
                null
        );
        return new ResponseEntity<>(publicResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleUsernameExistException(UsernameExistException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Username already exists: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                requestId,
                null
        );
        return new ResponseEntity<>(publicResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleEmailExistException(EmailExistException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Email already exists: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTimeZoneException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleInvalidTimeZoneException(InvalidTimeZoneException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Invalid time zone: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleInvalidCredentialsException(InvalidCredentialsException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Invalid credentials: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmployerNotFoundException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleEmployerNotFoundException(EmployerNotFoundException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Employer not found: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleJobNotFoundException(JobNotFoundException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Job not found: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FreelancerNotFoundException.class)
    public ResponseEntity<PublicResponseDTO<String>> handleFreelancerNotFoundException(FreelancerNotFoundException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Freelancer not found: {}", path, e);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        PublicResponseDTO<String> publicResponseDTO = new PublicResponseDTO<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                requestId,
                null
        );

        return new ResponseEntity<>(publicResponseDTO, HttpStatus.NOT_FOUND);
    }
}
