package dev.dwidi.jobportal.service;

import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalRequestDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.Freelancer;
import dev.dwidi.jobportal.entity.Job;
import dev.dwidi.jobportal.entity.Proposal;
import dev.dwidi.jobportal.entity.enums.ProposalStatus;
import dev.dwidi.jobportal.exception.*;
import dev.dwidi.jobportal.repository.FreelancerRepository;
import dev.dwidi.jobportal.repository.JobRepository;
import dev.dwidi.jobportal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private TimeZoneUtil timeZoneUtil;

    public PublicResponseDTO<ProposalResponseDTO> createProposal(ProposalRequestDTO proposalRequestDTO, String timeZone) {
        Job job = jobRepository.findById(proposalRequestDTO.getJobId())
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        Freelancer freelancer = freelancerRepository.findById(proposalRequestDTO.getFreelancerId())
                .orElseThrow(() -> new FreelancerNotFoundException("Freelancer not found"));

        if (proposalRepository.existsByFreelancerAndJob(freelancer, job)) {
            throw new ProposalAlreadyExistsException("You have already submitted a proposal for this job");
        }

        Proposal proposal = new Proposal();
        proposal.setTitle(proposalRequestDTO.getTitle());
        proposal.setDescription(proposalRequestDTO.getDescription());
        proposal.setStatus(ProposalStatus.APPLIED);
        proposal.setJob(job);
        proposal.setFreelancer(freelancer);
        proposal.setCreatedAt(ZonedDateTime.now());
        proposal.setUpdatedAt(ZonedDateTime.now());

        Proposal savedProposal = proposalRepository.save(proposal);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(savedProposal.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(savedProposal.getUpdatedAt(), timeZone);

        // Build the response
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO(
                savedProposal.getProposalId(),
                savedProposal.getTitle(),
                savedProposal.getDescription(),
                savedProposal.getStatus(),
                createdAtLocal,
                updatedAtLocal,
                savedProposal.getFreelancer().getFreelancerId(),
                savedProposal.getJob().getJobId()
        );

        return new PublicResponseDTO<>(201, "Proposal created successfully", requestId, proposalResponseDTO);
    }

    public PublicResponseDTO<ProposalResponseDTO> updateProposal(Long proposalId, ProposalRequestDTO proposalRequestDTO, String timeZone) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ProposalNotFoundException("Proposal not found"));

        if (proposalRequestDTO.getTitle() != null) {
            proposal.setTitle(proposalRequestDTO.getTitle());
        }
        if (proposalRequestDTO.getDescription() != null) {
            proposal.setDescription(proposalRequestDTO.getDescription());
        }

        proposal.setUpdatedAt(ZonedDateTime.now());

        Proposal updatedProposal = proposalRepository.save(proposal);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(updatedProposal.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(updatedProposal.getUpdatedAt(), timeZone);

        // Build the response
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO(
                updatedProposal.getProposalId(),
                updatedProposal.getTitle(),
                updatedProposal.getDescription(),
                updatedProposal.getStatus(),
                createdAtLocal,
                updatedAtLocal,
                updatedProposal.getFreelancer().getFreelancerId(),
                updatedProposal.getJob().getJobId()
        );

        return new PublicResponseDTO<>(200, "Proposal updated successfully", requestId, proposalResponseDTO);
    }

    public PublicResponseDTO<List<ProposalResponseDTO>> getProposalsByFreelancer(Long freelancerId, String timeZone) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException("Freelancer not found"));

        List<Proposal> proposals = proposalRepository.findByFreelancer(freelancer);
        List<ProposalResponseDTO> proposalResponseDTOs = proposals.stream().map(proposal -> {
            String createdAtLocal = timeZoneUtil.convertToLocalTime(proposal.getCreatedAt(), timeZone);
            String updatedAtLocal = timeZoneUtil.convertToLocalTime(proposal.getUpdatedAt(), timeZone);
            return new ProposalResponseDTO(
                    proposal.getProposalId(),
                    proposal.getTitle(),
                    proposal.getDescription(),
                    proposal.getStatus(),
                    createdAtLocal,
                    updatedAtLocal,
                    proposal.getFreelancer().getFreelancerId(),
                    proposal.getJob().getJobId()
            );
        }).collect(Collectors.toList());

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        return new PublicResponseDTO<>(200, "Proposals retrieved successfully", requestId, proposalResponseDTOs);
    }

    public PublicResponseDTO<List<ProposalResponseDTO>> getProposalsByStatus(String status, String timeZone) {
        ProposalStatus proposalStatus;
        try {
            proposalStatus = ProposalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProposalStatusException("Invalid proposal status: " + status);
        }

        List<Proposal> proposals = proposalRepository.findByStatus(proposalStatus);
        List<ProposalResponseDTO> proposalResponseDTOs = proposals.stream().map(proposal -> {
            String createdAtLocal = timeZoneUtil.convertToLocalTime(proposal.getCreatedAt(), timeZone);
            String updatedAtLocal = timeZoneUtil.convertToLocalTime(proposal.getUpdatedAt(), timeZone);
            return new ProposalResponseDTO(
                    proposal.getProposalId(),
                    proposal.getTitle(),
                    proposal.getDescription(),
                    proposal.getStatus(),
                    createdAtLocal,
                    updatedAtLocal,
                    proposal.getFreelancer().getFreelancerId(),
                    proposal.getJob().getJobId()
            );
        }).collect(Collectors.toList());

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        return new PublicResponseDTO<>(200, "Proposals retrieved successfully", requestId, proposalResponseDTOs);
    }

    public PublicResponseDTO<?> getAllProposals(Integer page, Integer size, String timeZone) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Proposal> proposalsPage = proposalRepository.findAll(pageable);

            Page<ProposalResponseDTO> proposalResponseDTOs = proposalsPage.map(proposal -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(proposal.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(proposal.getUpdatedAt(), timeZone);
                return new ProposalResponseDTO(
                        proposal.getProposalId(),
                        proposal.getTitle(),
                        proposal.getDescription(),
                        proposal.getStatus(),
                        createdAtLocal,
                        updatedAtLocal,
                        proposal.getFreelancer().getFreelancerId(),
                        proposal.getJob().getJobId()
                );
            });

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Proposals retrieved successfully", requestId, proposalResponseDTOs);
        } else {
            List<Proposal> proposals = proposalRepository.findAll();
            List<ProposalResponseDTO> proposalResponseDTOs = proposals.stream().map(proposal -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(proposal.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(proposal.getUpdatedAt(), timeZone);
                return new ProposalResponseDTO(
                        proposal.getProposalId(),
                        proposal.getTitle(),
                        proposal.getDescription(),
                        proposal.getStatus(),
                        createdAtLocal,
                        updatedAtLocal,
                        proposal.getFreelancer().getFreelancerId(),
                        proposal.getJob().getJobId()
                );
            }).collect(Collectors.toList());

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Proposals retrieved successfully", requestId, proposalResponseDTOs);
        }
    }

    public PublicResponseDTO<ProposalResponseDTO> changeProposalStatusToHired(Long proposalId, String timeZone) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ProposalNotFoundException("Proposal not found"));

        proposal.setStatus(ProposalStatus.HIRED);
        proposal.setUpdatedAt(ZonedDateTime.now());

        Proposal updatedProposal = proposalRepository.save(proposal);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(updatedProposal.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(updatedProposal.getUpdatedAt(), timeZone);

        // Build the response
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO(
                updatedProposal.getProposalId(),
                updatedProposal.getTitle(),
                updatedProposal.getDescription(),
                updatedProposal.getStatus(),
                createdAtLocal,
                updatedAtLocal,
                updatedProposal.getFreelancer().getFreelancerId(),
                updatedProposal.getJob().getJobId()
        );

        return new PublicResponseDTO<>(200, "Proposal status changed to HIRED successfully", requestId, proposalResponseDTO);
    }
}