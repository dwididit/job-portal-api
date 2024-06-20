package dev.dwidi.jobportal.dto.freelancer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerRequestDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private String skills;
}
