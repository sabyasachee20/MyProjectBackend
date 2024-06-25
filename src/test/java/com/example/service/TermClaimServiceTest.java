package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.model.TermClaim;
import com.example.model.UserPolicy;
import com.example.repo.TermClaimRepo;

@ExtendWith(MockitoExtension.class)
public class TermClaimServiceTest {

    @Mock
    private TermClaimRepo termClaimRepoMock;

    @Mock
    private UserPolicyService userPolicyServiceMock;

    @InjectMocks
    private TermClaimService termClaimService;

    private TermClaim sampleTermClaim;
    private UserPolicy sampleUserPolicy;

    @BeforeEach
    public void setUp() {
        sampleUserPolicy = new UserPolicy();
        sampleUserPolicy.setUserPolicyId(1L);
        sampleUserPolicy.setStartDate(LocalDate.of(2023, 1, 1)); // Sample start date
        sampleUserPolicy.setEndDate(LocalDate.of(2023, 12, 31)); // Sample end date
        sampleUserPolicy.setStatus("Active"); // Sample status

        sampleTermClaim = new TermClaim();
        sampleTermClaim.setTermClaimId(1L);
        sampleTermClaim.setUserPolicy(sampleUserPolicy);
        sampleTermClaim.setDateofdemise(LocalDate.of(2023, 6, 15)); // Sample date of demise
        sampleTermClaim.setStatus("Pending"); // Initial status
    }

    @Test
    public void testCreateTermClaim_Success() {
        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

        when(termClaimRepoMock.save(sampleTermClaim)).thenReturn(sampleTermClaim);

        TermClaim createdTermClaim = termClaimService.createTermClaim(sampleTermClaim);

        assertNotNull(createdTermClaim);
        assertEquals("Pending", createdTermClaim.getStatus());
        verify(userPolicyServiceMock, times(1)).readOne(sampleUserPolicy.getUserPolicyId());
        verify(termClaimRepoMock, times(1)).save(sampleTermClaim);
    }

    @Test
    public void testCreateTermClaim_UserPolicyNotFound() {
        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            termClaimService.createTermClaim(sampleTermClaim);
        });

        verify(termClaimRepoMock, never()).save(any());
    }

    @Test
    public void testCreateTermClaim_UserPolicyNotActive() {
        sampleUserPolicy.setStatus("Inactive");

        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

        assertThrows(IllegalArgumentException.class, () -> {
            termClaimService.createTermClaim(sampleTermClaim);
        });

        verify(termClaimRepoMock, never()).save(any());
    }
    @Test
    public void testCreateTermClaim_DateOfDemiseAfterEndDate() {
        sampleUserPolicy.setEndDate(LocalDate.of(2023, 6, 10));

        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

        assertThrows(IllegalArgumentException.class, () -> {
            termClaimService.createTermClaim(sampleTermClaim);
        }, "Term claim date must be on or before the end date of the user policy.");

        verify(termClaimRepoMock, never()).save(any());
    }
    @Test
    public void testCreateTermClaim_DateOfDemiseBeforeStartDate() {
        sampleUserPolicy.setStartDate(LocalDate.of(2023, 6, 20));

        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

        assertThrows(IllegalArgumentException.class, () -> {
            termClaimService.createTermClaim(sampleTermClaim);
        }, "Term claim date must be on or after the start date of the user policy.");

        verify(termClaimRepoMock, never()).save(any());
    }
}
