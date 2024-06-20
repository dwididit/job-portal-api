package dev.dwidi.jobportal.controller.auth;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerLoginResponseDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterRequestDTO;
import dev.dwidi.jobportal.dto.employer.EmployerRegisterResponseDTO;
import dev.dwidi.jobportal.service.auth.EmployerAuthService;
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

class EmployerAuthControllerTest {

    @Mock
    private EmployerAuthService employerAuthService;

    @InjectMocks
    private EmployerAuthController employerAuthController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employerAuthController).build();
    }

    @Test
    void testRegisterEmployer() throws Exception {
        EmployerRegisterRequestDTO registerRequestDTO = new EmployerRegisterRequestDTO();
        EmployerRegisterResponseDTO registerResponseDTO = new EmployerRegisterResponseDTO();
        PublicResponseDTO<EmployerRegisterResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", registerResponseDTO);

        when(employerAuthService.registerEmployer(any(EmployerRegisterRequestDTO.class), any(String.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/employer/register")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{ \"username\": \"john.doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginEmployer() throws Exception {
        EmployerLoginRequestDTO loginRequestDTO = new EmployerLoginRequestDTO();
        EmployerLoginResponseDTO loginResponseDTO = new EmployerLoginResponseDTO();
        PublicResponseDTO<EmployerLoginResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", loginResponseDTO);

        when(employerAuthService.loginEmployer(any(EmployerLoginRequestDTO.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/employer/login")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .content("{ \"username\": \"john.doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshToken() throws Exception {
        EmployerLoginResponseDTO loginResponseDTO = new EmployerLoginResponseDTO();
        PublicResponseDTO<EmployerLoginResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Success", "requestId", loginResponseDTO);

        when(employerAuthService.refreshToken(any(String.class)))
                .thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/auth/employer/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer some-refresh-token"))
                .andExpect(status().isOk());
    }
}
