package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerDetailResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRequestDTO;
import dev.dwidi.jobportal.service.EmployerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Employer API", description = "API for managing employers")
@RequestMapping("/api/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @Operation(summary = "Get all employers", description = "Retrieve all employers with optional pagination and time zone conversion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employers",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<?>> getAllEmployers(
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<?> response = employerService.getAllEmployers(page, size, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Update an employer", description = "Update employer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated employer"),
            @ApiResponse(responseCode = "404", description = "Employer not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - Username or Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })


    @PutMapping("/update/{employerId}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<EmployerDetailResponseDTO>> updateEmployer(
            @Parameter(description = "Employer ID") @PathVariable Long employerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated employer details", required = true)
            @RequestBody EmployerRequestDTO employerRequestDTO,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<EmployerDetailResponseDTO> response = employerService.updateEmployer(employerId, employerRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
