package dev.dwidi.jobportal.dto.freelancer;

import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.Freelancer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerDetailResponseDTO {
    private Long freelancerId;
    private String name;
    private String email;
    private String username;
    private String skills;
    private String createdAt;
    private String updatedAt;
    private String userRole;
    private List<ProposalResponseDTO> proposals;

    public FreelancerDetailResponseDTO(Freelancer freelancer) {
        this.freelancerId = freelancer.getFreelancerId();
        this.name = freelancer.getName();
        this.email = freelancer.getEmail();
        this.username = freelancer.getUsername();
        this.skills = freelancer.getSkills();
        this.createdAt = String.valueOf(freelancer.getCreatedAt());
        this.updatedAt = String.valueOf(freelancer.getUpdatedAt());
        this.userRole = freelancer.getUserRole();
        this.proposals = freelancer.getProposals().stream()
                .map(ProposalResponseDTO::new)
                .collect(Collectors.toList());
    }
}
