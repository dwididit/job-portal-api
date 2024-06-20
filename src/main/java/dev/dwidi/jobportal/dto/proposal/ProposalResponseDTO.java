package dev.dwidi.jobportal.dto.proposal;

import dev.dwidi.jobportal.entity.Proposal;
import dev.dwidi.jobportal.entity.enums.ProposalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalResponseDTO {
    private Long proposalId;
    private String title;
    private String description;
    private ProposalStatus status;
    private String createdAt;
    private String updatedAt;
    private Long freelancerId;
    private Long jobId;

    public ProposalResponseDTO(Proposal proposal) {
        this.proposalId = proposal.getProposalId();
        this.title = proposal.getTitle();
        this.description = proposal.getDescription();
        this.status = proposal.getStatus();
        this.createdAt = String.valueOf(proposal.getCreatedAt());
        this.updatedAt = String.valueOf(proposal.getUpdatedAt());
        this.freelancerId = proposal.getFreelancer().getFreelancerId();
        this.jobId = proposal.getJob().getJobId();
    }
}