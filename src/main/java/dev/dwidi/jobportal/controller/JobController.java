package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.job.JobRequestDTO;
import dev.dwidi.jobportal.dto.job.JobResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.enums.JobStatus;
import dev.dwidi.jobportal.service.JobService;
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

import java.util.List;

@RestController
@Tag(name = "Job API", description = "API for managing jobs")
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Operation(summary = "Create a new job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created job",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<JobResponseDTO>> createJob(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Job details", required = true)
            @RequestBody JobRequestDTO jobRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<JobResponseDTO> response = jobService.createJob(jobRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Update an existing job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated job",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/update/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<JobResponseDTO>> updateJob(
            @Parameter(description = "Job ID") @PathVariable Long jobId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated job details", required = true)
            @RequestBody JobRequestDTO jobRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<JobResponseDTO> response = jobService.updateJob(jobId, jobRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get jobs by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping()
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<List<JobResponseDTO>>> getJobsByStatus(
            @Parameter(description = "Job status") @RequestParam JobStatus status,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<List<JobResponseDTO>> response = jobService.getJobsByStatus(String.valueOf(status), timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get jobs by employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<List<JobResponseDTO>>> getJobsByEmployer(
            @Parameter(description = "Employer ID") @PathVariable Long employerId,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<List<JobResponseDTO>> response = jobService.getJobsByEmployer(employerId, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get proposals by job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposals",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{jobId}/proposal")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<List<ProposalResponseDTO>>> getProposalsByJob(
            @Parameter(description = "Job ID") @PathVariable Long jobId,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<List<ProposalResponseDTO>> response = jobService.getProposalsByJob(jobId, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get all jobs with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<?>> getAllJobs(
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(required = false) Integer page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(required = false) Integer size,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<?> response = jobService.getAllJobs(page, size, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
