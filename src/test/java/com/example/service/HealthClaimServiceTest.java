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

import com.example.model.HealthClaim;
import com.example.model.UserPolicy;
import com.example.repo.HealthClaimRepo;

@ExtendWith(MockitoExtension.class)
public class HealthClaimServiceTest {

    @Mock
    private HealthClaimRepo healthClaimRepoMock;

    @Mock
    private UserPolicyService userPolicyServiceMock;

    @InjectMocks
    private HealthClaimService healthClaimService;

    private HealthClaim sampleHealthClaim;
    private UserPolicy sampleUserPolicy;

    @BeforeEach
    public void setUp() {
        sampleUserPolicy = new UserPolicy();
        sampleUserPolicy.setUserPolicyId(1L);
        sampleUserPolicy.setStartDate(LocalDate.of(2023, 1, 1)); // Sample start date
        sampleUserPolicy.setEndDate(LocalDate.of(2023, 12, 31)); // Sample end date
        sampleUserPolicy.setLeftcoverage(1000); // Sample left coverage
        sampleUserPolicy.setStatus("Active"); // Sample status

        sampleHealthClaim = new HealthClaim();
        sampleHealthClaim.setHealthClaimId(1L);
        sampleHealthClaim.setUserPolicy(sampleUserPolicy);
        sampleHealthClaim.setDateofservice(LocalDate.of(2023, 6, 15)); // Sample date of service
        sampleHealthClaim.setClaimamt(500); // Sample claim amount
        sampleHealthClaim.setStatus("Pending"); // Initial status
    }


    @Test
    public void testCreateHealthClaim_Success() {
       
        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

       
        when(healthClaimRepoMock.save(sampleHealthClaim)).thenReturn(sampleHealthClaim);

       
        HealthClaim createdHealthClaim = healthClaimService.createHealthClaim(sampleHealthClaim);

       
        assertNotNull(createdHealthClaim);
        assertEquals("Pending", createdHealthClaim.getStatus());
        verify(userPolicyServiceMock, times(1)).readOne(sampleUserPolicy.getUserPolicyId());
        verify(healthClaimRepoMock, times(1)).save(sampleHealthClaim);
    }

   
    @Test
    public void testCreateHealthClaim_UserPolicyNotFound() {
        
        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            healthClaimService.createHealthClaim(sampleHealthClaim);
        });
        verify(healthClaimRepoMock, never()).save(any());
    }

    @Test
    public void testCreateHealthClaim_UserPolicyNotActive() {
        sampleUserPolicy.setStatus("Inactive");

        when(userPolicyServiceMock.readOne(sampleUserPolicy.getUserPolicyId())).thenReturn(sampleUserPolicy);

        assertThrows(IllegalArgumentException.class, () -> {
            healthClaimService.createHealthClaim(sampleHealthClaim);
        });

        verify(healthClaimRepoMock, never()).save(any());
    }

}
