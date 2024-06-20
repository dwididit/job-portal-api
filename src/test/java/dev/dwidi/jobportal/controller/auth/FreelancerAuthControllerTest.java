package dev.dwidi.jobportal.controller.auth;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerLoginResponseDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.freelancer.FreelancerRegisterResponseDTO;
import dev.dwidi.jobportal.service.auth.FreelancerAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FreelancerAuthControllerTest {

    @Mock
    private FreelancerAuthService freelancerAuthService;

    @InjectMocks
    private FreelancerAuthController freelancerAuthController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(freelancerAuthController).build();
    }

    @Test
    void testRegisterFreelancer() throws Exception {
        FreelancerRegisterRequestDTO registerRequestDTO = new FreelancerRegisterRequestDTO();
        FreelancerRegisterResponseDTO registerResponseDTO = new FreelancerRegisterResponseDTO();
        PublicResponseDTO<FreelancerRegisterResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", registerResponseDTO);

        when(freelancerAuthService.registerFreelancer(any(FreelancerRegisterRequestDTO.class), any(String.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/freelancer/register")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"username\": \"jane.doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginFreelancer() throws Exception {
        FreelancerLoginRequestDTO loginRequestDTO = new FreelancerLoginRequestDTO();
        FreelancerLoginResponseDTO loginResponseDTO = new FreelancerLoginResponseDTO();
        PublicResponseDTO<FreelancerLoginResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", loginResponseDTO);

        when(freelancerAuthService.loginFreelancer(any(FreelancerLoginRequestDTO.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/freelancer/login")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .content("{ \"username\": \"jane.doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshToken() throws Exception {
        FreelancerLoginResponseDTO loginResponseDTO = new FreelancerLoginResponseDTO();
        PublicResponseDTO<FreelancerLoginResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", loginResponseDTO);

        when(freelancerAuthService.refreshToken(any(String.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/freelancer/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer some-refresh-token"))
                .andExpect(status().isOk());
    }
}
