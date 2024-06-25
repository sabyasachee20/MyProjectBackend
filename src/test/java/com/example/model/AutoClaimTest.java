package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AutoClaimTest {

    @InjectMocks
    private AutoClaim autoClaim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long autoClaimId = 1L;
        String vehicleModelNo = "XYZ123";
        String licensePlateNo = "ABC1234";
        double exShowroomPrice = 1500000.0;
        int vehicleAge = 3;
        LocalDate incidentTime = LocalDate.of(2023, 6, 15);
        int driverAge = 30;
        String damageDescription = "Front bumper damage";
        double damageCost = 2500.0;
        String status = "Pending";
        String photoOfDamage = "damage_photo.jpg";
        UserPolicy userPolicy = new UserPolicy();

        autoClaim.setAutoClaimId(autoClaimId);
        autoClaim.setVehicleModelNo(vehicleModelNo);
        autoClaim.setLicensePlateNo(licensePlateNo);
        autoClaim.setExShowroomPrice(exShowroomPrice);
        autoClaim.setVehicleAge(vehicleAge);
        autoClaim.setIncidentTime(incidentTime);
        autoClaim.setDriverAge(driverAge);
        autoClaim.setDamageDescription(damageDescription);
        autoClaim.setDamageCost(damageCost);
        autoClaim.setStatus(status);
        autoClaim.setPhotoOfDamage(photoOfDamage);
        autoClaim.setUserPolicy(userPolicy);

        assertEquals(autoClaimId, autoClaim.getAutoClaimId());
        assertEquals(vehicleModelNo, autoClaim.getVehicleModelNo());
        assertEquals(licensePlateNo, autoClaim.getLicensePlateNo());
        assertEquals(exShowroomPrice, autoClaim.getExShowroomPrice());
        assertEquals(vehicleAge, autoClaim.getVehicleAge());
        assertEquals(incidentTime, autoClaim.getIncidentTime());
        assertEquals(driverAge, autoClaim.getDriverAge());
        assertEquals(damageDescription, autoClaim.getDamageDescription());
        assertEquals(damageCost, autoClaim.getDamageCost());
        assertEquals(status, autoClaim.getStatus());
        assertEquals(photoOfDamage, autoClaim.getPhotoOfDamage());
        assertEquals(userPolicy, autoClaim.getUserPolicy());
    }

    @Test
    void testNoArgsConstructor() {
        AutoClaim autoClaim = new AutoClaim();
        assertNotNull(autoClaim);
    }

    @Test
    void testAllArgsConstructor() {
        Long autoClaimId = 1L;
        String vehicleModelNo = "XYZ123";
        String licensePlateNo = "ABC1234";
        double exShowroomPrice = 1500000.0;
        int vehicleAge = 3;
        LocalDate incidentTime = LocalDate.of(2023, 6, 15);
        int driverAge = 30;
        String damageDescription = "Front bumper damage";
        double damageCost = 2500.0;
        String status = "Pending";
        String photoOfDamage = "damage_photo.jpg";
        UserPolicy userPolicy = new UserPolicy();

        AutoClaim autoClaim = new AutoClaim(autoClaimId, vehicleModelNo, licensePlateNo, exShowroomPrice, vehicleAge,
                incidentTime, driverAge, damageDescription, damageCost, status, photoOfDamage, userPolicy);

        assertEquals(autoClaimId, autoClaim.getAutoClaimId());
        assertEquals(vehicleModelNo, autoClaim.getVehicleModelNo());
        assertEquals(licensePlateNo, autoClaim.getLicensePlateNo());
        assertEquals(exShowroomPrice, autoClaim.getExShowroomPrice());
        assertEquals(vehicleAge, autoClaim.getVehicleAge());
        assertEquals(incidentTime, autoClaim.getIncidentTime());
        assertEquals(driverAge, autoClaim.getDriverAge());
        assertEquals(damageDescription, autoClaim.getDamageDescription());
        assertEquals(damageCost, autoClaim.getDamageCost());
        assertEquals(status, autoClaim.getStatus());
        assertEquals(photoOfDamage, autoClaim.getPhotoOfDamage());
        assertEquals(userPolicy, autoClaim.getUserPolicy());
    }
}
