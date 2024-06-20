package dev.dwidi.jobportal.dto.freelancer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerRegisterRequestDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private String skill;
}
