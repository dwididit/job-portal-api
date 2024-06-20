package dev.dwidi.jobportal.controller.auth;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterResponseDTO;
import dev.dwidi.jobportal.service.auth.FreelancerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Freelancer Authentication API", description = "API for freelancer authentication")
@RequestMapping("/api/auth/freelancer")
public class FreelancerAuthController {

    @Autowired
    private FreelancerAuthService freelancerAuthService;

    @Operation(summary = "Register a new freelancer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<PublicResponseDTO<FreelancerRegisterResponseDTO>> registerFreelancer(
            @RequestBody(description = "Freelancer registration details", required = true)
            @org.springframework.web.bind.annotation.RequestBody FreelancerRegisterRequestDTO freelancerRegisterRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<FreelancerRegisterResponseDTO> response = freelancerAuthService.registerFreelancer(freelancerRegisterRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Login a freelancer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<PublicResponseDTO<FreelancerLoginResponseDTO>> loginFreelancer(
            @RequestBody(description = "Freelancer login details", required = true)
            @org.springframework.web.bind.annotation.RequestBody FreelancerLoginRequestDTO freelancerLoginRequestDTO) {

        PublicResponseDTO<FreelancerLoginResponseDTO> response = freelancerAuthService.loginFreelancer(freelancerLoginRequestDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Refresh freelancer's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<PublicResponseDTO<FreelancerLoginResponseDTO>> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader) {

        PublicResponseDTO<FreelancerLoginResponseDTO> response = freelancerAuthService.refreshToken(authorizationHeader);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
