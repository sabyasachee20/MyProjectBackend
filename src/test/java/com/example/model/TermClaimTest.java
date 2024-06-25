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
public class TermClaimTest {

    @InjectMocks
    private TermClaim termClaim;

    @Mock
    private UserPolicy mockUserPolicy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long termClaimId = 1L;
        LocalDate dateOfDemise = LocalDate.of(2023, 6, 20);
        String deathProof = "death_proof.pdf";
        String nomineeProof = "nominee_proof.pdf";
        String status = "Pending";

        termClaim.setTermClaimId(termClaimId);
        termClaim.setDateofdemise(dateOfDemise);
        termClaim.setDeathProof(deathProof);
        termClaim.setNomineeProof(nomineeProof);
        termClaim.setStatus(status);
        termClaim.setUserPolicy(mockUserPolicy);

        assertEquals(termClaimId, termClaim.getTermClaimId());
        assertEquals(dateOfDemise, termClaim.getDateofdemise());
        assertEquals(deathProof, termClaim.getDeathProof());
        assertEquals(nomineeProof, termClaim.getNomineeProof());
        assertEquals(status, termClaim.getStatus());
        assertEquals(mockUserPolicy, termClaim.getUserPolicy());
    }

    @Test
    void testNoArgsConstructor() {
        TermClaim termClaim = new TermClaim();
        assertNotNull(termClaim);
    }

    @Test
    void testAllArgsConstructor() {
        Long termClaimId = 1L;
        LocalDate dateOfDemise = LocalDate.of(2023, 6, 20);
        String deathProof = "death_proof.pdf";
        String nomineeProof = "nominee_proof.pdf";
        String status = "Pending";

        TermClaim termClaim = new TermClaim(termClaimId, dateOfDemise, deathProof, nomineeProof, status, mockUserPolicy);

        assertEquals(termClaimId, termClaim.getTermClaimId());
        assertEquals(dateOfDemise, termClaim.getDateofdemise());
        assertEquals(deathProof, termClaim.getDeathProof());
        assertEquals(nomineeProof, termClaim.getNomineeProof());
        assertEquals(status, termClaim.getStatus());
        assertEquals(mockUserPolicy, termClaim.getUserPolicy());
    }
}

