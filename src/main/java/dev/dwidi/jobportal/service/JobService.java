package dev.dwidi.jobportal.service;

import dev.dwidi.jobportal.config.util.RequestIdGenerator;
import dev.dwidi.jobportal.config.util.TimeZoneUtil;
import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.job.JobRequestDTO;
import dev.dwidi.jobportal.dto.job.JobResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.Employer;
import dev.dwidi.jobportal.entity.Job;
import dev.dwidi.jobportal.entity.enums.JobStatus;
import dev.dwidi.jobportal.exception.EmployerNotFoundException;
import dev.dwidi.jobportal.exception.InvalidJobStatusException;
import dev.dwidi.jobportal.exception.JobNotFoundException;
import dev.dwidi.jobportal.repository.EmployerRepository;
import dev.dwidi.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Autowired
    private TimeZoneUtil timeZoneUtil;

    public PublicResponseDTO<JobResponseDTO> createJob(JobRequestDTO jobRequestDTO, String timeZone) {
        Employer employer = employerRepository.findByEmployerId(jobRequestDTO.getEmployerId())
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found"));

        // Validate the provided timeZone
        timeZoneUtil.validateTimeZone(timeZone);

        JobStatus jobStatus;
        try {
            jobStatus = JobStatus.valueOf(String.valueOf(jobRequestDTO.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new InvalidJobStatusException("Invalid job status: " + jobRequestDTO.getStatus());
        }

        Job job = new Job();
        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setStatus(jobStatus);
        job.setEmployer(employer);

        Job savedJob = jobRepository.save(job);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(savedJob.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(savedJob.getUpdatedAt(), timeZone);

        // Build the response
        JobResponseDTO jobResponseDTO = new JobResponseDTO(
                savedJob.getJobId(),
                savedJob.getTitle(),
                savedJob.getDescription(),
                savedJob.getStatus(),
                createdAtLocal,
                updatedAtLocal,
                savedJob.getEmployer().getEmployerId(),
                savedJob.getProposals().stream()
                        .map(ProposalResponseDTO::new).collect(Collectors.toList())
        );
        return new PublicResponseDTO<>(201, "Job created successfully", requestId, jobResponseDTO);
    }

    public PublicResponseDTO<JobResponseDTO> updateJob(Long jobId, JobRequestDTO jobRequestDTO, String timeZone) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        if (jobRequestDTO.getTitle() != null) {
            job.setTitle(jobRequestDTO.getTitle());
        }
        if (jobRequestDTO.getDescription() != null) {
            job.setDescription(jobRequestDTO.getDescription());
        }
        if (jobRequestDTO.getStatus() != null) {
            try {
                JobStatus jobStatus = JobStatus.valueOf(String.valueOf(jobRequestDTO.getStatus()));
                job.setStatus(jobStatus);
            } catch (IllegalArgumentException e) {
                throw new InvalidJobStatusException("Invalid job status: " + jobRequestDTO.getStatus());
            }
        }

        job.setUpdatedAt(ZonedDateTime.now());

        Job updatedJob = jobRepository.save(job);

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        // Convert times to the client's timezone
        String createdAtLocal = timeZoneUtil.convertToLocalTime(updatedJob.getCreatedAt(), timeZone);
        String updatedAtLocal = timeZoneUtil.convertToLocalTime(updatedJob.getUpdatedAt(), timeZone);

        // Build the response
        JobResponseDTO jobResponseDTO = new JobResponseDTO(
                updatedJob.getJobId(),
                updatedJob.getTitle(),
                updatedJob.getDescription(),
                updatedJob.getStatus(),
                createdAtLocal,
                updatedAtLocal,
                updatedJob.getEmployer().getEmployerId(),
                updatedJob.getProposals().stream()
                        .map(ProposalResponseDTO::new).collect(Collectors.toList())
        );
        return new PublicResponseDTO<>(200, "Job updated successfully", requestId, jobResponseDTO);
    }

    public PublicResponseDTO<List<JobResponseDTO>> getJobsByStatus(String status, String timeZone) {
        JobStatus jobStatus;
        try {
            jobStatus = JobStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidJobStatusException("Invalid job status: " + status);
        }

        List<Job> jobs = jobRepository.findByStatus(jobStatus);
        List<JobResponseDTO> responseDTOs = jobs.stream().map(job -> {
            String createdAtLocal = timeZoneUtil.convertToLocalTime(job.getCreatedAt(), timeZone);
            String updatedAtLocal = timeZoneUtil.convertToLocalTime(job.getUpdatedAt(), timeZone);
            return new JobResponseDTO(
                    job.getJobId(),
                    job.getTitle(),
                    job.getDescription(),
                    job.getStatus(),
                    createdAtLocal,
                    updatedAtLocal,
                    job.getEmployer().getEmployerId(),
                    job.getProposals().stream().map(ProposalResponseDTO::new).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        return new PublicResponseDTO<>(200, "Jobs retrieved successfully", requestId, responseDTOs);
    }

    public PublicResponseDTO<List<JobResponseDTO>> getJobsByEmployer(Long employerId, String timeZone) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found"));

        List<Job> jobs = employer.getJobs();
        List<JobResponseDTO> responseDTOs = jobs.stream().map(job -> {
            String createdAtLocal = timeZoneUtil.convertToLocalTime(job.getCreatedAt(), timeZone);
            String updatedAtLocal = timeZoneUtil.convertToLocalTime(job.getUpdatedAt(), timeZone);
            return new JobResponseDTO(
                    job.getJobId(),
                    job.getTitle(),
                    job.getDescription(),
                    job.getStatus(),
                    createdAtLocal,
                    updatedAtLocal,
                    job.getEmployer().getEmployerId(),
                    job.getProposals().stream().map(ProposalResponseDTO::new).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        return new PublicResponseDTO<>(200, "Jobs retrieved successfully", requestId, responseDTOs);
    }

    public PublicResponseDTO<List<ProposalResponseDTO>> getProposalsByJob(Long jobId, String timeZone) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        List<ProposalResponseDTO> proposals = job.getProposals().stream()
                .map(proposal -> {
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
                })
                .collect(Collectors.toList());

        // Generate a unique requestId for tracing
        String requestId = RequestIdGenerator.generateRequestId();

        return new PublicResponseDTO<>(200, "Proposals retrieved successfully", requestId, proposals);
    }


    public PublicResponseDTO<?> getAllJobs(Integer page, Integer size, String timeZone) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Job> jobsPage = jobRepository.findAll(pageable);

            Page<JobResponseDTO> jobResponseDTOs = jobsPage.map(job -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(job.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(job.getUpdatedAt(), timeZone);
                return new JobResponseDTO(
                        job.getJobId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getStatus(),
                        createdAtLocal,
                        updatedAtLocal,
                        job.getEmployer().getEmployerId(),
                        job.getProposals().stream().map(ProposalResponseDTO::new).collect(Collectors.toList())
                );
            });

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Jobs retrieved successfully", requestId, jobResponseDTOs);
        } else {
            List<Job> jobs = jobRepository.findAll();
            List<JobResponseDTO> jobResponseDTOs = jobs.stream().map(job -> {
                String createdAtLocal = timeZoneUtil.convertToLocalTime(job.getCreatedAt(), timeZone);
                String updatedAtLocal = timeZoneUtil.convertToLocalTime(job.getUpdatedAt(), timeZone);
                return new JobResponseDTO(
                        job.getJobId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getStatus(),
                        createdAtLocal,
                        updatedAtLocal,
                        job.getEmployer().getEmployerId(),
                        job.getProposals().stream().map(ProposalResponseDTO::new).collect(Collectors.toList())
                );
            }).collect(Collectors.toList());

            // Generate a unique requestId for tracing
            String requestId = RequestIdGenerator.generateRequestId();

            return new PublicResponseDTO<>(200, "Jobs retrieved successfully", requestId, jobResponseDTOs);
        }
    }
}
