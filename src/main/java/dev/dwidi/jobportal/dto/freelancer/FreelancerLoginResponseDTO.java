package dev.dwidi.jobportal.dto.freelancer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerLoginResponseDTO {
    private String accessToken;
    private String refreshToken;
}
