package dev.dwidi.jobportal.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalRequestDTO {
    private String title;
    private String description;
    private Long freelancerId;
    private Long jobId;
}
