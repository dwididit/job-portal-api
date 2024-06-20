package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.job.JobRequestDTO;
import dev.dwidi.jobportal.dto.job.JobResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.entity.enums.JobStatus;
import dev.dwidi.jobportal.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    void testCreateJob() throws Exception {
        JobRequestDTO jobRequestDTO = new JobRequestDTO();
        JobResponseDTO jobResponseDTO = new JobResponseDTO();
        PublicResponseDTO<JobResponseDTO> publicResponseDTO = new PublicResponseDTO<>(201, "Job created successfully", "requestId", jobResponseDTO);

        when(jobService.createJob(any(JobRequestDTO.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/job/create")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"title\": \"Software Engineer\", \"description\": \"Develop and maintain software\" }"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateJob() throws Exception {
        JobRequestDTO jobRequestDTO = new JobRequestDTO();
        JobResponseDTO jobResponseDTO = new JobResponseDTO();
        PublicResponseDTO<JobResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Job updated successfully", "requestId", jobResponseDTO);

        when(jobService.updateJob(any(Long.class), any(JobRequestDTO.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(put("/api/job/update/1")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"title\": \"Senior Software Engineer\", \"description\": \"Lead the development team\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetJobsByStatus() throws Exception {
        List<JobResponseDTO> jobResponseDTOList = List.of(new JobResponseDTO());
        PublicResponseDTO<List<JobResponseDTO>> publicResponseDTO = new PublicResponseDTO<>(200, "Jobs retrieved successfully", "requestId", jobResponseDTOList);

        when(jobService.getJobsByStatus(any(String.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(get("/api/job")
                        .header("Time-Zone", "Asia/Jakarta")
                        .param("status", JobStatus.PUBLISHED.name()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetJobsByEmployer() throws Exception {
        List<JobResponseDTO> jobResponseDTOList = List.of(new JobResponseDTO());
        PublicResponseDTO<List<JobResponseDTO>> publicResponseDTO = new PublicResponseDTO<>(200, "Jobs retrieved successfully", "requestId", jobResponseDTOList);

        when(jobService.getJobsByEmployer(any(Long.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(get("/api/job/employer/1")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProposalsByJob() throws Exception {
        List<ProposalResponseDTO> proposalResponseDTOList = List.of(new ProposalResponseDTO());
        PublicResponseDTO<List<ProposalResponseDTO>> publicResponseDTO = new PublicResponseDTO<>(200, "Proposals retrieved successfully", "requestId", proposalResponseDTOList);

        when(jobService.getProposalsByJob(any(Long.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(get("/api/job/1/proposal")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }
}
