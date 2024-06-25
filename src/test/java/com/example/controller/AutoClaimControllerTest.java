package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.model.AutoClaim;
import com.example.model.UserPolicy;
import com.example.service.AutoClaimService;
import com.example.service.UserPolicyService;

@ExtendWith(MockitoExtension.class)
public class AutoClaimControllerTest {

    @Mock
    private AutoClaimService autoClaimService;

    @Mock
    private UserPolicyService userPolicyService;

    @InjectMocks
    private AutoClaimController autoClaimController;

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
    void createAutoClaim_shouldCreateAutoClaim_whenValid() throws IOException {
        MockMultipartFile photoOfDamage = new MockMultipartFile(
                "photoOfDamage",
                "damage.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(userPolicyService.getUserPolicyById(1L)).thenReturn(Optional.of(userPolicy));
        when(autoClaimService.createAutoClaim(any(AutoClaim.class))).thenReturn(autoClaim);

        ResponseEntity<Object> response = autoClaimController.createAutoClaim(
                "Model X",
                "ABC123",
                50000,
                2,
                LocalDate.of(2023, 1, 1),
                30,
                "Front bumper damage",
                2000,
                "Pending",
                photoOfDamage,
                1L
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof AutoClaim);
        AutoClaim createdClaim = (AutoClaim) response.getBody();
        assertEquals("Model X", createdClaim.getVehicleModelNo());
        verify(autoClaimService, times(1)).createAutoClaim(any(AutoClaim.class));
    }

    @Test
    void createAutoClaim_shouldReturnBadRequest_whenPhotoOfDamageIsEmpty() throws IOException {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "photoOfDamage",
                "",
                "image/jpeg",
                new byte[0]
        );

        ResponseEntity<Object> response = autoClaimController.createAutoClaim(
                "Model X",
                "ABC123",
                50000,
                2,
                LocalDate.of(2023, 1, 1),
                30,
                "Front bumper damage",
                2000,
                "Pending",
                emptyFile,
                1L
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Photo of damage is required", response.getBody());
        verify(autoClaimService, times(0)).createAutoClaim(any(AutoClaim.class));
    }

    @Test
    void createAutoClaim_shouldReturnBadRequest_whenUserPolicyNotFound() throws IOException {
        MockMultipartFile photoOfDamage = new MockMultipartFile(
                "photoOfDamage",
                "damage.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(userPolicyService.getUserPolicyById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = autoClaimController.createAutoClaim(
                "Model X",
                "ABC123",
                50000,
                2,
                LocalDate.of(2023, 1, 1),
                30,
                "Front bumper damage",
                2000,
                "Pending",
                photoOfDamage,
                1L
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid user policy ID", response.getBody());
        verify(autoClaimService, times(0)).createAutoClaim(any(AutoClaim.class));
    }

    @Test
    void getAllAutoClaims_shouldReturnAllAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimService.getAllAutoClaims()).thenReturn(claims);

        List<AutoClaim> result = autoClaimController.getAllAutoClaims();

        assertEquals(1, result.size());
        assertEquals("Model X", result.get(0).getVehicleModelNo());
        verify(autoClaimService, times(1)).getAllAutoClaims();
    }

    @Test
    void getAutoClaimsByUserPolicyId_shouldReturnAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimService.findAutoClaimsByUserPolicyId(1L)).thenReturn(claims);

        List<AutoClaim> result = autoClaimController.getAutoClaimsByUserPolicyId(1L);

        assertEquals(1, result.size());
        assertEquals("Model X", result.get(0).getVehicleModelNo());
        verify(autoClaimService, times(1)).findAutoClaimsByUserPolicyId(1L);
    }

    @Test
    void expireAutoClaimsByUserPolicyId_shouldExpireAutoClaims() {
        List<AutoClaim> claims = Arrays.asList(autoClaim);
        when(autoClaimService.findAutoClaimsByUserPolicyIdAndExpire(1L)).thenReturn(claims);

        List<AutoClaim> result = autoClaimController.expireAutoClaimsByUserPolicyId(1L);

        assertEquals(1, result.size());
        assertEquals("Model X", result.get(0).getVehicleModelNo());
        verify(autoClaimService, times(1)).findAutoClaimsByUserPolicyIdAndExpire(1L);
    }
}
