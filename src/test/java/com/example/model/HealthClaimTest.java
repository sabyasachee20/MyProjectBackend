package com.example.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HealthClaimTest {

    @InjectMocks
    private HealthClaim healthClaim;

    @Mock
    private UserPolicy mockUserPolicy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long healthClaimId = 1L;
        String disease = "COVID-19";
        LocalDate dateOfService = LocalDate.of(2023, 6, 20);
        String hospitalName = "City Hospital";
        String address = "123 Main St, City";
        String doctorInCharge = "Dr. Smith";
        String medicalBill = "medical_bill.pdf";
        String status = "Pending";
        double claimAmt = 2500.0;

        healthClaim.setHealthClaimId(healthClaimId);
        healthClaim.setDisease(disease);
        healthClaim.setDateofservice(dateOfService);
        healthClaim.setHospitalname(hospitalName);
        healthClaim.setAddress(address);
        healthClaim.setDoctorincharge(doctorInCharge);
        healthClaim.setMedicalbill(medicalBill);
        healthClaim.setStatus(status);
        healthClaim.setClaimamt(claimAmt);
        healthClaim.setUserPolicy(mockUserPolicy);

        assertEquals(healthClaimId, healthClaim.getHealthClaimId());
        assertEquals(disease, healthClaim.getDisease());
        assertEquals(dateOfService, healthClaim.getDateofservice());
        assertEquals(hospitalName, healthClaim.getHospitalname());
        assertEquals(address, healthClaim.getAddress());
        assertEquals(doctorInCharge, healthClaim.getDoctorincharge());
        assertEquals(medicalBill, healthClaim.getMedicalbill());
        assertEquals(status, healthClaim.getStatus());
        assertEquals(claimAmt, healthClaim.getClaimamt());
        assertEquals(mockUserPolicy, healthClaim.getUserPolicy());
    }

    @Test
    void testNoArgsConstructor() {
        HealthClaim healthClaim = new HealthClaim();
        assertNotNull(healthClaim);
    }

    @Test
    void testAllArgsConstructor() {
        Long healthClaimId = 1L;
        String disease = "COVID-19";
        LocalDate dateOfService = LocalDate.of(2023, 6, 20);
        String hospitalName = "City Hospital";
        String address = "123 Main St, City";
        String doctorInCharge = "Dr. Smith";
        String medicalBill = "medical_bill.pdf";
        String status = "Pending";
        double claimAmt = 2500.0;

        HealthClaim healthClaim = new HealthClaim(healthClaimId, disease, dateOfService, hospitalName, address,
                doctorInCharge, medicalBill, status, claimAmt, mockUserPolicy);

        assertEquals(healthClaimId, healthClaim.getHealthClaimId());
        assertEquals(disease, healthClaim.getDisease());
        assertEquals(dateOfService, healthClaim.getDateofservice());
        assertEquals(hospitalName, healthClaim.getHospitalname());
        assertEquals(address, healthClaim.getAddress());
        assertEquals(doctorInCharge, healthClaim.getDoctorincharge());
        assertEquals(medicalBill, healthClaim.getMedicalbill());
        assertEquals(status, healthClaim.getStatus());
        assertEquals(claimAmt, healthClaim.getClaimamt());
        assertEquals(mockUserPolicy, healthClaim.getUserPolicy());
    }
}

