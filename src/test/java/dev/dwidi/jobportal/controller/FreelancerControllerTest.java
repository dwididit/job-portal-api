package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerDetailResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRequestDTO;
import dev.dwidi.jobportal.service.FreelancerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FreelancerControllerTest {

    @Mock
    private FreelancerService freelancerService;

    @InjectMocks
    private FreelancerController freelancerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(freelancerController).build();
    }

    @Test
    void testGetAllFreelancers() throws Exception {
        when(freelancerService.getAllFreelancers(any(), any(), any())).thenReturn(new PublicResponseDTO<>(200, "Success", "requestId", null));

        mockMvc.perform(get("/api/freelancer/all")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFreelancer() throws Exception {
        FreelancerRequestDTO freelancerRequestDTO = new FreelancerRequestDTO();
        FreelancerDetailResponseDTO freelancerDetailResponseDTO = new FreelancerDetailResponseDTO();

        when(freelancerService.updateFreelancer(eq(1L), any(FreelancerRequestDTO.class), any(String.class)))
                .thenReturn(new PublicResponseDTO<>(200, "Success", "requestId", freelancerDetailResponseDTO));

        mockMvc.perform(put("/api/freelancer/update/{freelancerId}", 1L)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"name\": \"John Doe\" }"))
                .andExpect(status().isOk());
    }
}
