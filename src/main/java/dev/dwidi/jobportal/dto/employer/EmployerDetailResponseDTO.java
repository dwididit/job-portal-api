package dev.dwidi.jobportal.dto.employer;

import dev.dwidi.jobportal.dto.job.JobResponseDTO;
import dev.dwidi.jobportal.entity.Employer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetailResponseDTO {
    private Long employerId;
    private String name;
    private String email;
    private String username;
    private String companyName;
    private String createdAt;
    private String updatedAt;
    private String userRole;
    private List<JobResponseDTO> jobs;

    public EmployerDetailResponseDTO(Employer employer) {
        this.employerId = employer.getEmployerId();
        this.name = employer.getName();
        this.email = employer.getEmail();
        this.username = employer.getUsername();
        this.companyName = employer.getCompanyName();
        this.createdAt = String.valueOf(employer.getCreatedAt());
        this.updatedAt = String.valueOf(employer.getUpdatedAt());
        this.userRole = employer.getUserRole();
        this.jobs = employer.getJobs().stream()
                .map(JobResponseDTO::new)
                .collect(Collectors.toList());
    }
}
