package com.example.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.model.TermClaim;
import com.example.model.UserPolicy;
import com.example.service.TermClaimService;
import com.example.service.UserPolicyService;

@ExtendWith(MockitoExtension.class)
public class TermClaimControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TermClaimService termClaimService;

    @Mock
    private UserPolicyService userPolicyService;

    @Value("${react.public.folder}")
    private String reactPublicFolder;

    @InjectMocks
    private TermClaimController termClaimController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(termClaimController).build();
    }

    @Test
    void testCreateTermClaim_Success() throws Exception {
        LocalDate dateOfDemise = LocalDate.of(2024, 6, 25);
        MockMultipartFile deathProof = new MockMultipartFile("deathProof", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Test file".getBytes());
        MockMultipartFile nomineeProof = new MockMultipartFile("nomineeProof", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Test file".getBytes());

        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setUserPolicyId(1L);

        when(userPolicyService.getUserPolicyById(anyLong())).thenReturn(Optional.of(userPolicy));
        when(termClaimService.createTermClaim(any(TermClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(multipart("/term-claims/create")
                .file(deathProof)
                .file(nomineeProof)
                .param("dateofdemise", dateOfDemise.toString())
                .param("status", "Pending")
                .param("userPolicyId", String.valueOf(userPolicy.getUserPolicyId())))
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("$.dateofdemise").value(dateOfDemise.toString()))
                .andExpect(jsonPath("$.status").value("Pending"));

        verify(userPolicyService, times(1)).getUserPolicyById(anyLong());
        verify(termClaimService, times(1)).createTermClaim(any(TermClaim.class));
    }

    @Test
    void testCreateTermClaim_NoProofDocuments() throws Exception {
        mockMvc.perform(multipart("/term-claims/create")
                .param("dateofdemise", "2024-06-25")
                .param("status", "Pending")
                .param("userPolicyId", "1"))
                .andExpect(status().isBadRequest());
            //    .andExpect(content().string("Death proof and nominee proof are required"));

        verify(userPolicyService, never()).getUserPolicyById(anyLong());
        verify(termClaimService, never()).createTermClaim(any(TermClaim.class));
    }

    @Test
    void testGetAllTermClaims() throws Exception {
        List<TermClaim> termClaims = Arrays.asList(
                new TermClaim(1L, LocalDate.now(), "assets/images/deathProof1.pdf", "assets/images/nomineeProof1.pdf", "Pending", null),
                new TermClaim(2L, LocalDate.now(), "assets/images/deathProof2.pdf", "assets/images/nomineeProof2.pdf", "Approved", null));

        when(termClaimService.getAllTermClaims()).thenReturn(termClaims);

        mockMvc.perform(get("/term-claims/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(termClaims.size()))
                .andExpect(jsonPath("$[0].deathProof").value(termClaims.get(0).getDeathProof()))
                .andExpect(jsonPath("$[1].deathProof").value(termClaims.get(1).getDeathProof()));

        verify(termClaimService, times(1)).getAllTermClaims();
    }

}

