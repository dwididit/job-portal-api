package dev.dwidi.jobportal.service;

import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerDetailResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRequestDTO;
import dev.dwidi.jobportal.entity.Freelancer;
import dev.dwidi.jobportal.exception.EmailExistException;
import dev.dwidi.jobportal.exception.FreelancerNotFoundException;
import dev.dwidi.jobportal.exception.UsernameExistException;
import dev.dwidi.jobportal.repository.FreelancerRepository;
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
public class FreelancerService {

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TimeZoneUtil timeZoneUtil;

    public PublicResponseDTO<?> getAllFreelancers(Integer page, Integer size, String timeZone) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Freelancer> freelancersPage = freelancerRepository.findAll(pageable);

            Page<FreelancerDetailResponseDTO> freelancerResponseDTOs = freelancersPage.map(freelancer -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(freelancer.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(freelancer.getUpdatedAt(), timeZone);
                FreelancerDetailResponseDTO responseDTO = new FreelancerDetailResponseDTO(freelancer);
                responseDTO.setCreatedAt(createdAtLocal);
                responseDTO.setUpdatedAt(updatedAtLocal);
                return responseDTO;
            });

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Freelancers retrieved successfully", requestId, freelancerResponseDTOs);
        } else {
            List<Freelancer> freelancers = freelancerRepository.findAll();
            List<FreelancerDetailResponseDTO> freelancerResponseDTOs = freelancers.stream().map(freelancer -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(freelancer.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(freelancer.getUpdatedAt(), timeZone);
                FreelancerDetailResponseDTO responseDTO = new FreelancerDetailResponseDTO(freelancer);
                responseDTO.setCreatedAt(createdAtLocal);
                responseDTO.setUpdatedAt(updatedAtLocal);
                return responseDTO;
            }).collect(Collectors.toList());

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Freelancers retrieved successfully", requestId, freelancerResponseDTOs);
        }
    }

    public PublicResponseDTO<FreelancerDetailResponseDTO> updateFreelancer(Long freelancerId, FreelancerRequestDTO freelancerRequestDTO, String timeZone) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException("Freelancer not found"));

        if (freelancerRequestDTO.getUsername() != null && !freelancer.getUsername().equals(freelancerRequestDTO.getUsername()) && freelancerRepository.existsByUsername(freelancerRequestDTO.getUsername())) {
            throw new UsernameExistException("Username already exists");
        }

        if (freelancerRequestDTO.getEmail() != null && !freelancer.getEmail().equals(freelancerRequestDTO.getEmail()) && freelancerRepository.existsByEmail(freelancerRequestDTO.getEmail())) {
            throw new EmailExistException("Email already exists");
        }

        if (freelancerRequestDTO.getName() != null) {
            freelancer.setName(freelancerRequestDTO.getName());
        }
        if (freelancerRequestDTO.getEmail() != null) {
            freelancer.setEmail(freelancerRequestDTO.getEmail());
        }
        if (freelancerRequestDTO.getUsername() != null) {
            freelancer.setUsername(freelancerRequestDTO.getUsername());
        }
        if (freelancerRequestDTO.getPassword() != null) {
            freelancer.setPassword(passwordEncoder.encode(freelancerRequestDTO.getPassword()));
        }
        if (freelancerRequestDTO.getSkills() != null) {
            freelancer.setSkills(freelancerRequestDTO.getSkills());
        }

        freelancer.setUpdatedAt(ZonedDateTime.now());

        Freelancer updatedFreelancer = freelancerRepository.save(freelancer);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(updatedFreelancer.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(updatedFreelancer.getUpdatedAt(), timeZone);

        FreelancerDetailResponseDTO responseDTO = new FreelancerDetailResponseDTO(updatedFreelancer);
        responseDTO.setCreatedAt(createdAtLocal);
        responseDTO.setUpdatedAt(updatedAtLocal);

        return new PublicResponseDTO<>(200, "Freelancer updated successfully", requestId, responseDTO);
    }
}
