package dev.dwidi.jobportal.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterResponseDTO;
import dev.dwidi.jobportal.entity.Freelancer;
import dev.dwidi.jobportal.entity.enums.UserStatus;
import dev.dwidi.jobportal.exception.EmailExistException;
import dev.dwidi.jobportal.exception.InvalidCredentialsException;
import dev.dwidi.jobportal.exception.UsernameExistException;
import dev.dwidi.jobportal.repository.FreelancerRepository;
import dev.dwidi.jobportal.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FreelancerAuthService {

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private TimeZoneUtil timeZoneUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private JWTUtil jwtUtil;

    public PublicResponseDTO<FreelancerRegisterResponseDTO> registerFreelancer(FreelancerRegisterRequestDTO freelancerRegisterRequestDTO, String timeZone) {
        if (freelancerRepository.existsByUsername(freelancerRegisterRequestDTO.getUsername())) {
            throw new UsernameExistException("Username already exists");
        }

        if (freelancerRepository.existsByEmail(freelancerRegisterRequestDTO.getEmail())) {
            throw new EmailExistException("Email already exists");
        }

        // Validate the provided timeZone
        timeZoneUtil.validateTimeZone(timeZone);

        Freelancer freelancer = new Freelancer();
        freelancer.setName(freelancerRegisterRequestDTO.getName());
        freelancer.setEmail(freelancerRegisterRequestDTO.getEmail());
        freelancer.setUsername(freelancerRegisterRequestDTO.getUsername());
        freelancer.setPassword(passwordEncoder.encode(freelancerRegisterRequestDTO.getPassword()));
        freelancer.setSkills(freelancerRegisterRequestDTO.getSkill());
        freelancer.setUserRole(String.valueOf(UserStatus.ROLE_FREELANCER));

        Freelancer savedFreelancer = freelancerRepository.save(freelancer);

        String requestId = RequestIdGenerator.generateRequestId();

        String createdAtLocal = timeZoneUtil.convertToLocalTime(savedFreelancer.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(savedFreelancer.getUpdatedAt(), timeZone);

        FreelancerRegisterResponseDTO registerResponseDTO = new FreelancerRegisterResponseDTO(
                savedFreelancer.getFreelancerId(),
                savedFreelancer.getName(),
                savedFreelancer.getEmail(),
                savedFreelancer.getUsername(),
                savedFreelancer.getSkills(),
                savedFreelancer.getUserRole(),
                createdAtLocal,
                updatedAtLocal
        );

        return new PublicResponseDTO<>(201, "Freelancer registered successfully", requestId, registerResponseDTO);
    }

    public PublicResponseDTO<FreelancerLoginResponseDTO> loginFreelancer(FreelancerLoginRequestDTO loginRequestDTO) {
        Freelancer freelancer = freelancerRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), freelancer.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateAccessToken(freelancer.getUsername(), freelancer.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(freelancer.getUsername());

        FreelancerLoginResponseDTO loginResponseDTO = new FreelancerLoginResponseDTO(accessToken, refreshToken);

        String requestId = RequestIdGenerator.generateRequestId();
        return new PublicResponseDTO<>(200, "Login successful", requestId, loginResponseDTO);
    }

    public PublicResponseDTO<FreelancerLoginResponseDTO> refreshToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new PublicResponseDTO<>(401, "Invalid token", null, null);
        }

        String refreshToken = authorizationHeader.substring(7);

        DecodedJWT decodedJWT = jwtUtil.verifyToken(refreshToken);

        if (!decodedJWT.getClaim("type").asString().equals("REFRESH")) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String username = decodedJWT.getSubject();
        Freelancer freelancer = freelancerRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        String newAccessToken = jwtUtil.generateAccessToken(username, freelancer.getUserRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        FreelancerLoginResponseDTO loginResponseDTO = new FreelancerLoginResponseDTO(newAccessToken, newRefreshToken);

        String requestId = RequestIdGenerator.generateRequestId();
        return new PublicResponseDTO<>(200, "Token refreshed successfully", requestId, loginResponseDTO);
    }
}
