package dev.dwidi.jobportal.dto.employer;

import dev.dwidi.jobportal.entity.Employer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRegisterResponseDTO {
    private Long employerId;
    private String name;
    private String email;
    private String username;
    private String companyName;
    private String userRole;
    private String createdAt;
    private String updatedAt;

    public EmployerRegisterResponseDTO(Employer employer) {
        this.employerId = employer.getEmployerId();
        this.name = employer.getName();
        this.email = employer.getEmail();
        this.username = employer.getUsername();
        this.companyName = employer.getCompanyName();
        this.userRole = employer.getUserRole();
        this.createdAt = String.valueOf(employer.getCreatedAt());
        this.updatedAt = String.valueOf(employer.getUpdatedAt());
    }
}
