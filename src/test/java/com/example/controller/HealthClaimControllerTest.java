package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.HealthClaim;
import com.example.model.UserPolicy;
import com.example.service.HealthClaimService;
import com.example.service.UserPolicyService;

@ExtendWith(MockitoExtension.class)
public class HealthClaimControllerTest {

    @Mock
    private HealthClaimService healthClaimService;

    @Mock
    private UserPolicyService userPolicyService;

    @InjectMocks
    private HealthClaimController healthClaimController;

    private HealthClaim testHealthClaim;
    private UserPolicy testUserPolicy;

    @BeforeEach
    void setUp() {
        testHealthClaim = new HealthClaim();
        testHealthClaim.setHealthClaimId(1L);
        testHealthClaim.setDisease("Fever");
        testHealthClaim.setDateofservice(LocalDate.now());
        testHealthClaim.setHospitalname("Test Hospital");
        testHealthClaim.setAddress("Test Address");
        testHealthClaim.setDoctorincharge("Dr. John Doe");
        testHealthClaim.setStatus("Pending");
        testHealthClaim.setClaimamt(1000.0);
        testHealthClaim.setMedicalbill("assets/images/test.jpg");

        testUserPolicy = new UserPolicy();
        testUserPolicy.setUserPolicyId(1L);
    }

    @Test
    void testCreateHealthClaim_Success() throws IOException {
        when(userPolicyService.getUserPolicyById(anyLong())).thenReturn(Optional.of(testUserPolicy));
        when(healthClaimService.createHealthClaim(any(HealthClaim.class))).thenReturn(testHealthClaim);
        MultipartFile mockFile = new MockMultipartFile("medicalBill", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes());
        ResponseEntity<Object> responseEntity = healthClaimController.createHealthClaim(
                "Fever", LocalDate.now(), "Test Hospital", "Test Address", "Dr. John Doe", "Pending", 1000.0, mockFile, 1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        HealthClaim createdHealthClaim = (HealthClaim) responseEntity.getBody();
        assertNotNull(createdHealthClaim);
        assertEquals(testHealthClaim.getHealthClaimId(), createdHealthClaim.getHealthClaimId());
        verify(userPolicyService, times(1)).getUserPolicyById(anyLong());
        verify(healthClaimService, times(1)).createHealthClaim(any(HealthClaim.class));
    }

    @Test
    void testCreateHealthClaim_MissingMedicalBill() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("medicalBill", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
        ResponseEntity<Object> responseEntity = healthClaimController.createHealthClaim(
                "Fever", LocalDate.now(), "Test Hospital", "Test Address", "Dr. John Doe", "Pending", 1000.0, mockFile, 1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Photo of medical bill is required", responseEntity.getBody());
        verify(userPolicyService, never()).getUserPolicyById(anyLong());
        verify(healthClaimService, never()).createHealthClaim(any(HealthClaim.class));
    }

    @Test
    void testCreateHealthClaim_InvalidUserPolicyId() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("medicalBill", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes());
        when(userPolicyService.getUserPolicyById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = healthClaimController.createHealthClaim(
                "Fever", LocalDate.now(), "Test Hospital", "Test Address", "Dr. John Doe", "Pending", 1000.0, mockFile, 1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid user policy ID", responseEntity.getBody());
        verify(userPolicyService, times(1)).getUserPolicyById(anyLong());
        verify(healthClaimService, never()).createHealthClaim(any(HealthClaim.class));
    }

    @Test
    void testGetAllHealthClaims() {
        List<HealthClaim> healthClaims = new ArrayList<>();
        healthClaims.add(testHealthClaim);
        when(healthClaimService.getAllHealthClaims()).thenReturn(healthClaims);

        List<HealthClaim> retrievedHealthClaims = healthClaimController.getAllHealthClaims();

        assertNotNull(retrievedHealthClaims);
        assertFalse(retrievedHealthClaims.isEmpty());
        assertEquals(healthClaims.size(), retrievedHealthClaims.size());
        assertEquals(testHealthClaim.getHealthClaimId(), retrievedHealthClaims.get(0).getHealthClaimId());

        verify(healthClaimService, times(1)).getAllHealthClaims();
    }

    @Test
    void testGetHealthClaimsByUserPolicyId() {
        List<HealthClaim> healthClaims = new ArrayList<>();
        healthClaims.add(testHealthClaim);
        when(healthClaimService.findHealthClaimsByUserPolicyId(anyLong())).thenReturn(healthClaims);

        List<HealthClaim> retrievedHealthClaims = healthClaimController.getHealthClaimsByUserPolicyId(1L);

        assertNotNull(retrievedHealthClaims);
        assertFalse(retrievedHealthClaims.isEmpty());
        assertEquals(healthClaims.size(), retrievedHealthClaims.size());
        assertEquals(testHealthClaim.getHealthClaimId(), retrievedHealthClaims.get(0).getHealthClaimId());

        verify(healthClaimService, times(1)).findHealthClaimsByUserPolicyId(anyLong());
    }

    @Test
    void testExpireHealthClaimsByUserPolicyId() {
        List<HealthClaim> healthClaims = new ArrayList<>();
        healthClaims.add(testHealthClaim);
        when(healthClaimService.findHealthClaimsByUserPolicyIdAndExpire(anyLong())).thenReturn(healthClaims);

        List<HealthClaim> expiredHealthClaims = healthClaimController.expireHealthClaimsByUserPolicyId(1L);
        assertNotNull(expiredHealthClaims);
        assertFalse(expiredHealthClaims.isEmpty());
        assertEquals(healthClaims.size(), expiredHealthClaims.size());
        assertEquals(testHealthClaim.getHealthClaimId(), expiredHealthClaims.get(0).getHealthClaimId());

        verify(healthClaimService, times(1)).findHealthClaimsByUserPolicyIdAndExpire(anyLong());
    }
}
