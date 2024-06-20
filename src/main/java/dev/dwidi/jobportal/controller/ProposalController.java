package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalRequestDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.enums.ProposalStatus;
import dev.dwidi.jobportal.service.ProposalService;
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
@Tag(name = "Proposal API", description = "API for managing proposals")
@RequestMapping("/api/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @Operation(summary = "Create a new proposal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created proposal",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<ProposalResponseDTO>> createProposal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Proposal details", required = true)
            @RequestBody ProposalRequestDTO proposalRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<ProposalResponseDTO> response = proposalService.createProposal(proposalRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Update an existing proposal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated proposal",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Proposal not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/update/{proposalId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<ProposalResponseDTO>> updateProposal(
            @Parameter(description = "Proposal ID") @PathVariable Long proposalId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated proposal details", required = true)
            @RequestBody ProposalRequestDTO proposalRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<ProposalResponseDTO> response = proposalService.updateProposal(proposalId, proposalRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get proposals by freelancer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposals",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Freelancer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/freelancer/{freelancerId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<List<ProposalResponseDTO>>> getProposalsByFreelancer(
            @Parameter(description = "Freelancer ID") @PathVariable Long freelancerId,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<List<ProposalResponseDTO>> response = proposalService.getProposalsByFreelancer(freelancerId, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get proposals by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposals",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<List<ProposalResponseDTO>>> getProposalsByStatus(
            @Parameter(description = "Proposal status") @RequestParam ProposalStatus status,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<List<ProposalResponseDTO>> response = proposalService.getProposalsByStatus(String.valueOf(status), timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Get all proposals with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposals",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<?>> getAllProposals(
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "Time zone for converting timestamps", example = "Asia/Jakarta") @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<?> response = proposalService.getAllProposals(page, size, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Change proposal status to hired")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proposal status changed to hired successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Proposal not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/change-status/{proposalId}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<PublicResponseDTO<ProposalResponseDTO>> changeProposalStatusToHired(
            @PathVariable Long proposalId,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<ProposalResponseDTO> response = proposalService.changeProposalStatusToHired(proposalId, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
