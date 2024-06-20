package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerDetailResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRequestDTO;
import dev.dwidi.jobportal.service.EmployerService;
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

class EmployerControllerTest {

    @Mock
    private EmployerService employerService;

    @InjectMocks
    private EmployerController employerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employerController).build();
    }

    @Test
    void testGetAllEmployers() throws Exception {
        when(employerService.getAllEmployers(any(), any(), any())).thenReturn(new PublicResponseDTO<>(200, "Success", "requestId", null));

        mockMvc.perform(get("/api/employer/all")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEmployer() throws Exception {
        EmployerRequestDTO employerRequestDTO = new EmployerRequestDTO();
        EmployerDetailResponseDTO employerDetailResponseDTO = new EmployerDetailResponseDTO();

        when(employerService.updateEmployer(eq(1L), any(EmployerRequestDTO.class), any(String.class)))
                .thenReturn(new PublicResponseDTO<>(200, "Success", "requestId", employerDetailResponseDTO));

        mockMvc.perform(put("/api/employer/update/{employerId}", 1L)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"name\": \"John Doe\" }"))
                .andExpect(status().isOk());
    }
}
