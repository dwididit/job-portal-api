package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerDetailResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRequestDTO;
import dev.dwidi.jobportal.service.FreelancerService;
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
@Tag(name = "Freelancer API", description = "API for managing freelancers")
@RequestMapping("/api/freelancer")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Operation(summary = "Get all freelancers", description = "Retrieve all freelancers with optional pagination and time zone conversion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved freelancers",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<?>> getAllFreelancers(
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<?> response = freelancerService.getAllFreelancers(page, size, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Update a freelancer", description = "Update freelancer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated freelancer"),
            @ApiResponse(responseCode = "404", description = "Freelancer not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - Username or Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/{freelancerId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<FreelancerDetailResponseDTO>> updateFreelancer(
            @Parameter(description = "Freelancer ID") @PathVariable Long freelancerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated freelancer details", required = true)
            @RequestBody FreelancerRequestDTO freelancerRequestDTO,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<FreelancerDetailResponseDTO> response = freelancerService.updateFreelancer(freelancerId, freelancerRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
