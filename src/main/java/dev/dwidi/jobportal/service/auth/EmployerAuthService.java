package dev.dwidi.jobportal.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterResponseDTO;
import dev.dwidi.jobportal.entity.Employer;
import dev.dwidi.jobportal.entity.enums.UserStatus;
import dev.dwidi.jobportal.exception.EmailExistException;
import dev.dwidi.jobportal.exception.InvalidCredentialsException;
import dev.dwidi.jobportal.exception.UsernameExistException;
import dev.dwidi.jobportal.repository.EmployerRepository;
import dev.dwidi.jobportal.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployerAuthService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private TimeZoneUtil timeZoneUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private JWTUtil jwtUtil;

    public PublicResponseDTO<EmployerRegisterResponseDTO> registerEmployer(EmployerRegisterRequestDTO employerRegisterRequestDTO, String timeZone) {
        if (employerRepository.existsByUsername(employerRegisterRequestDTO.getUsername())) {
            throw new UsernameExistException("Username already exists");
        }

        if (employerRepository.existsByEmail(employerRegisterRequestDTO.getEmail())) {
            throw new EmailExistException("Email already exists");
        }

        // Validate the provided timeZone
        timeZoneUtil.validateTimeZone(timeZone);

        Employer employer = new Employer();
        employer.setName(employerRegisterRequestDTO.getName());
        employer.setEmail(employerRegisterRequestDTO.getEmail());
        employer.setUsername(employerRegisterRequestDTO.getUsername());
        employer.setPassword(passwordEncoder.encode(employerRegisterRequestDTO.getPassword()));
        employer.setCompanyName(employerRegisterRequestDTO.getCompanyName());
        employer.setUserRole(String.valueOf(UserStatus.ROLE_EMPLOYER));

        Employer savedEmployer = employerRepository.save(employer);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Use client's timezone to convert times
        String createdAtLocal = timeZoneUtil.convertToLocalTime(savedEmployer.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(savedEmployer.getUpdatedAt(), timeZone);

        // Build the response
        EmployerRegisterResponseDTO registerResponseDTO = new EmployerRegisterResponseDTO(
                savedEmployer.getEmployerId(),
                savedEmployer.getName(),
                savedEmployer.getEmail(),
                savedEmployer.getUsername(),
                savedEmployer.getCompanyName(),
                savedEmployer.getUserRole(),
                createdAtLocal,
                updatedAtLocal
        );

        return new PublicResponseDTO<>(201, "Employer registered successfully", requestId, registerResponseDTO);
    }

    public PublicResponseDTO<EmployerLoginResponseDTO> loginEmployer(EmployerLoginRequestDTO loginRequestDTO) {
        Employer employer = employerRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), employer.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateAccessToken(employer.getUsername(), employer.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(employer.getUsername());

        EmployerLoginResponseDTO loginResponseDTO = new EmployerLoginResponseDTO(accessToken, refreshToken);

        String requestId = RequestIdGenerator.generateRequestId();
        return new PublicResponseDTO<>(200, "Login successful", requestId, loginResponseDTO);
    }

    public PublicResponseDTO<EmployerLoginResponseDTO> refreshToken(String authorizationHeader) {
        // Extract the token from the Bearer header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new PublicResponseDTO<>(401, "Invalid token", null, null);
        }

        String refreshToken = authorizationHeader.substring(7);

        DecodedJWT decodedJWT = jwtUtil.verifyToken(refreshToken);

        if (!decodedJWT.getClaim("type").asString().equals("REFRESH")) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String username = decodedJWT.getSubject();
        Employer employer = employerRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        String newAccessToken = jwtUtil.generateAccessToken(username, employer.getUserRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        EmployerLoginResponseDTO loginResponseDTO = new EmployerLoginResponseDTO(newAccessToken, newRefreshToken);

        String requestId = RequestIdGenerator.generateRequestId();
        return new PublicResponseDTO<>(200, "Token refreshed successfully", requestId, loginResponseDTO);
    }
}
