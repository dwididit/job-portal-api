package dev.dwidi.jobportal.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRequestDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private String companyName;
}
