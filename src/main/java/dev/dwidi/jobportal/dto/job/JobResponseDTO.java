package dev.dwidi.jobportal.dto.job;

import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.Job;
import dev.dwidi.jobportal.entity.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDTO {
    private Long jobId;
    private String title;
    private String description;
    private JobStatus status;
    private String createdAt;
    private String updatedAt;
    private Long employerId;
    private List<ProposalResponseDTO> proposals;

    public JobResponseDTO(Job job) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.status = job.getStatus();
        this.createdAt = String.valueOf(job.getCreatedAt());
        this.updatedAt = String.valueOf(job.getUpdatedAt());
        this.employerId = job.getEmployer().getEmployerId();
        this.proposals = job.getProposals().stream()
                .map(ProposalResponseDTO::new)
                .collect(Collectors.toList());
    }
}
