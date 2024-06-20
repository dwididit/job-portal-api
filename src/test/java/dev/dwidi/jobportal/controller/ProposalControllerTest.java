package dev.dwidi.jobportal.controller;

import dev.dwidi.jobportal.dto.PublicResponseDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalRequestDTO;
import dev.dwidi.jobportal.dto.proposal.ProposalResponseDTO;
import dev.dwidi.jobportal.service.ProposalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProposalControllerTest {

    @Mock
    private ProposalService proposalService;

    @InjectMocks
    private ProposalController proposalController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(proposalController).build();
    }

    @Test
    void testCreateProposal() throws Exception {
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO();
        PublicResponseDTO<ProposalResponseDTO> publicResponseDTO = new PublicResponseDTO<>(201, "Proposal created successfully", "requestId", proposalResponseDTO);

        when(proposalService.createProposal(any(ProposalRequestDTO.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(post("/api/proposal/create")
                        .contentType("application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{\"title\":\"title\",\"description\":\"description\",\"freelancerId\":1,\"jobId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateProposal() throws Exception {
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO();
        PublicResponseDTO<ProposalResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Proposal updated successfully", "requestId", proposalResponseDTO);

        when(proposalService.updateProposal(eq(1L), any(ProposalRequestDTO.class), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(put("/api/proposal/update/1")
                        .contentType("application/json")
                        .header("Time-Zone", "Asia/Jakarta")
                        .content("{\"title\":\"title\",\"description\":\"description\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProposalsByFreelancer() throws Exception {
        List<ProposalResponseDTO> proposalResponseDTOList = List.of(new ProposalResponseDTO());
        PublicResponseDTO<List<ProposalResponseDTO>> publicResponseDTO = new PublicResponseDTO<>(200, "Proposals retrieved successfully", "requestId", proposalResponseDTOList);

        when(proposalService.getProposalsByFreelancer(eq(1L), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(get("/api/proposal/freelancer/1")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProposalsByStatus() throws Exception {
        List<ProposalResponseDTO> proposalResponseDTOList = List.of(new ProposalResponseDTO());
        PublicResponseDTO<List<ProposalResponseDTO>> publicResponseDTO = new PublicResponseDTO<>(200, "Proposals retrieved successfully", "requestId", proposalResponseDTOList);

        when(proposalService.getProposalsByStatus(eq("APPLIED"), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(get("/api/proposal")
                        .header("Time-Zone", "Asia/Jakarta")
                        .param("status", "APPLIED"))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeProposalStatusToHired() throws Exception {
        ProposalResponseDTO proposalResponseDTO = new ProposalResponseDTO();
        PublicResponseDTO<ProposalResponseDTO> publicResponseDTO = new PublicResponseDTO<>(200, "Proposal status changed to hired successfully", "requestId", proposalResponseDTO);

        when(proposalService.changeProposalStatusToHired(eq(1L), any(String.class))).thenReturn(publicResponseDTO);

        mockMvc.perform(put("/api/proposal/change-status/1")
                        .header("Time-Zone", "Asia/Jakarta"))
                .andExpect(status().isOk());
    }
}
