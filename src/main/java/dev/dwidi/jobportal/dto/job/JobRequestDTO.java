package dev.dwidi.jobportal.dto.job;

import dev.dwidi.jobportal.entity.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDTO {
    private String title;
    private String description;
    private JobStatus status;
    private Long employerId;
}
