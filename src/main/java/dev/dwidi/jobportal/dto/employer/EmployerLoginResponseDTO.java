package dev.dwidi.jobportal.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerLoginResponseDTO {
    private String accessToken;
    private String refreshToken;
}
