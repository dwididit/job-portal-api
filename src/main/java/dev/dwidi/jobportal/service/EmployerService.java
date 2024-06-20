package dev.dwidi.jobportal.service;

import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerDetailResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRequestDTO;
import dev.dwidi.jobportal.entity.Employer;
import dev.dwidi.jobportal.exception.EmailExistException;
import dev.dwidi.jobportal.exception.EmployerNotFoundException;
import dev.dwidi.jobportal.exception.UsernameExistException;
import dev.dwidi.jobportal.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private TimeZoneUtil timeZoneUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PublicResponseDTO<?> getAllEmployers(Integer page, Integer size, String timeZone) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employer> employersPage = employerRepository.findAll(pageable);

            Page<EmployerDetailResponseDTO> employerResponseDTOs = employersPage.map(employer -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(employer.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(employer.getUpdatedAt(), timeZone);
                EmployerDetailResponseDTO responseDTO = new EmployerDetailResponseDTO(employer);
                responseDTO.setCreatedAt(createdAtLocal);
                responseDTO.setUpdatedAt(updatedAtLocal);
                return responseDTO;
            });

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Employers retrieved successfully", requestId, employerResponseDTOs);
        } else {
            List<Employer> employers = employerRepository.findAll();
            List<EmployerDetailResponseDTO> employerResponseDTOs = employers.stream().map(employer -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(employer.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(employer.getUpdatedAt(), timeZone);
                EmployerDetailResponseDTO responseDTO = new EmployerDetailResponseDTO(employer);
                responseDTO.setCreatedAt(createdAtLocal);
                responseDTO.setUpdatedAt(updatedAtLocal);
                return responseDTO;
            }).collect(Collectors.toList());

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Employers retrieved successfully", requestId, employerResponseDTOs);
        }
    }

    public PublicResponseDTO<EmployerDetailResponseDTO> updateEmployer(Long employerId, EmployerRequestDTO employerRequestDTO, String timeZone) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found"));

        if (employerRequestDTO.getUsername() != null && !employer.getUsername().equals(employerRequestDTO.getUsername()) && employerRepository.existsByUsername(employerRequestDTO.getUsername())) {
            throw new UsernameExistException("Username already exists");
        }

        if (employerRequestDTO.getEmail() != null && !employer.getEmail().equals(employerRequestDTO.getEmail()) && employerRepository.existsByEmail(employerRequestDTO.getEmail())) {
            throw new EmailExistException("Email already exists");
        }

        if (employerRequestDTO.getName() != null) {
            employer.setName(employerRequestDTO.getName());
        }
        if (employerRequestDTO.getEmail() != null) {
            employer.setEmail(employerRequestDTO.getEmail());
        }
        if (employerRequestDTO.getUsername() != null) {
            employer.setUsername(employerRequestDTO.getUsername());
        }
        if (employerRequestDTO.getPassword() != null) {
            employer.setPassword(passwordEncoder.encode(employerRequestDTO.getPassword()));
        }
        if (employerRequestDTO.getCompanyName() != null) {
            employer.setCompanyName(employerRequestDTO.getCompanyName());
        }

        employer.setUpdatedAt(ZonedDateTime.now());

        Employer updatedEmployer = employerRepository.save(employer);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(updatedEmployer.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(updatedEmployer.getUpdatedAt(), timeZone);

        EmployerDetailResponseDTO responseDTO = new EmployerDetailResponseDTO(updatedEmployer);
        responseDTO.setCreatedAt(createdAtLocal);
        responseDTO.setUpdatedAt(updatedAtLocal);

        return new PublicResponseDTO<>(200, "Employer updated successfully", requestId, responseDTO);
    }
}
