package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.example.model.AutoClaim;
import com.example.model.UserPolicy;
import com.example.repo.AutoClaimRepo;

@ExtendWith(MockitoExtension.class)
class AutoClaimServiceTest {

    @Mock
    private AutoClaimRepo autoClaimRepository;

    @Mock
    private UserPolicyService userPolicyService;

    @InjectMocks
    private AutoClaimService autoClaimService;

    private AutoClaim autoClaim;
    private UserPolicy userPolicy;

    @BeforeEach
    void setUp() {
        userPolicy = new UserPolicy();
        userPolicy.setUserPolicyId(1L);
        userPolicy.setStatus("Active");
        userPolicy.setStartDate(LocalDate.of(2020, 1, 1));
        userPolicy.setEndDate(LocalDate.of(2025, 1, 1));
        userPolicy.setLeftcoverage(10000);

        autoClaim = new AutoClaim();
        autoClaim.setAutoClaimId(1L);
        autoClaim.setVehicleModelNo("Model X");
        autoClaim.setLicensePlateNo("ABC123");
        autoClaim.setExShowroomPrice(50000);
        autoClaim.setVehicleAge(2);
        autoClaim.setIncidentTime(LocalDate.of(2023, 1, 1));
        autoClaim.setDriverAge(30);
        autoClaim.setDamageDescription("Front bumper damage");
        autoClaim.setDamageCost(2000);
        autoClaim.setPhotoOfDamage("photo.jpg");
        autoClaim.setUserPolicy(userPolicy);
    }

    @Test
    void createAutoClaim_shouldCreateAutoClaim_whenValid() {
        when(userPolicyService.readOne(1L)).thenReturn(userPolicy);
        when(autoClaimRepository.save(any(AutoClaim.class))).thenReturn(autoClaim);//type check

        AutoClaim createdClaim = autoClaimService.createAutoClaim(autoClaim);

        assertNotNull(createdClaim);
        assertEquals("Pending", createdClaim.getStatus());
        verify(autoClaimRepository, times(1)).save(autoClaim);//save method of autoclaim repo is called once.
    }

    @Test
    void createAutoClaim_shouldThrowException_whenUserPolicyNotFound() {
        when(userPolicyService.readOne(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            autoClaimService.createAutoClaim(autoClaim);
        });

        assertEquals("UserPolicy not found for ID: 1", exception.getMessage());
    }

    @Test
    void createAutoClaim_shouldThrowException_whenUserPolicyNotActive() {
        userPolicy.setStatus("Inactive");
        when(userPolicyService.readOne(1L)).thenReturn(userPolicy);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            autoClaimService.createAutoClaim(autoClaim);
        });

        assertEquals("Your purchase status isn't Approved", exception.getMessage());
    }


    @Test
    void createAutoClaim_shouldThrowException_whenIncidentTimeIsBeforeStartDate() {
        autoClaim.setIncidentTime(LocalDate.of(2019, 1, 1));
        when(userPolicyService.readOne(1L)).thenReturn(userPolicy);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            autoClaimService.createAutoClaim(autoClaim);
        });

        assertEquals("Auto claim incident time must be on or after the start date:2020-01-01 of the user policy.", exception.getMessage());
    }

    @Test
    void createAutoClaim_shouldThrowException_whenDamageCostExceedsLeftCoverage() {
        autoClaim.setDamageCost(15000);
        when(userPolicyService.readOne(1L)).thenReturn(userPolicy);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            autoClaimService.createAutoClaim(autoClaim);
        });

        assertEquals("Damage cost exceeds the left coverage of the user policy.", exception.getMessage());
    }

    @Test
    void getAllAutoClaims_shouldReturnAllAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimRepository.findAll()).thenReturn(claims);

        List<AutoClaim> result = autoClaimService.getAllAutoClaims();

        assertEquals(1, result.size());
        verify(autoClaimRepository, times(1)).findAll();
    }

    @Test
    void findAutoClaimsByUserPolicyId_shouldReturnAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimRepository.findAutoClaimsByUserPolicyId(1L)).thenReturn(claims);

        List<AutoClaim> result = autoClaimService.findAutoClaimsByUserPolicyId(1L);

        assertEquals(1, result.size());
        verify(autoClaimRepository, times(1)).findAutoClaimsByUserPolicyId(1L);
    }

    @Test
    void findAutoClaimsByUserPolicyIdAndExpire_shouldExpireAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimRepository.findAutoClaimsByUserPolicyId(1L)).thenReturn(claims);
        when(autoClaimRepository.save(any(AutoClaim.class))).thenReturn(autoClaim);

        List<AutoClaim> result = autoClaimService.findAutoClaimsByUserPolicyIdAndExpire(1L);

        assertEquals(1, result.size());
        assertEquals("Expired", result.get(0).getStatus());
        verify(autoClaimRepository, times(1)).findAutoClaimsByUserPolicyId(1L);
        verify(autoClaimRepository, times(1)).save(autoClaim);
    }
}
