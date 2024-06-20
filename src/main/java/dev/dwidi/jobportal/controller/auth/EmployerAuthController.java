package dev.dwidi.jobportal.controller.auth;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterResponseDTO;
import dev.dwidi.jobportal.service.auth.EmployerAuthService;
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
@Tag(name = "Employer Authentication API", description = "API for employer authentication")
@RequestMapping("/api/auth/employer")
public class EmployerAuthController {

    @Autowired
    private EmployerAuthService employerAuthService;

    @Operation(summary = "Register a new employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<PublicResponseDTO<EmployerRegisterResponseDTO>> registerEmployer(
            @RequestBody(description = "Employer registration details", required = true)
            @org.springframework.web.bind.annotation.RequestBody EmployerRegisterRequestDTO employerRegisterRequestDTO,
            @RequestHeader(value = "Time-Zone", required = true) String timeZone) {

        PublicResponseDTO<EmployerRegisterResponseDTO> response = employerAuthService.registerEmployer(employerRegisterRequestDTO, timeZone);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Login an employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<PublicResponseDTO<EmployerLoginResponseDTO>> loginEmployer(
            @RequestBody(description = "Employer login details", required = true)
            @org.springframework.web.bind.annotation.RequestBody EmployerLoginRequestDTO employerLoginRequestDTO) {

        PublicResponseDTO<EmployerLoginResponseDTO> response = employerAuthService.loginEmployer(employerLoginRequestDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Refresh employer's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<PublicResponseDTO<EmployerLoginResponseDTO>> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader) {

        PublicResponseDTO<EmployerLoginResponseDTO> response = employerAuthService.refreshToken(authorizationHeader);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
