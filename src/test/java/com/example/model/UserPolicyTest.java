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
public class UserPolicyTest {

    @InjectMocks
    private UserPolicy userPolicy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long userPolicyId = 1L;
        float coverage = 0.85f;
        int term = 12;
        float premium = 5000.0f;
        String premiumTerm = "Yearly";
        int premiumCount = 1;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        String status = "Active";
        float leftCoverage = 0.6f;
        User user = new User();
        Policy policy = new Policy();

        userPolicy.setUserPolicyId(userPolicyId);
        userPolicy.setCoverage(coverage);
        userPolicy.setTerm(term);
        userPolicy.setPremium(premium);
        userPolicy.setPremiumTerm(premiumTerm);
        userPolicy.setPremiumCount(premiumCount);
        userPolicy.setStartDate(startDate);
        userPolicy.setEndDate(endDate);
        userPolicy.setStatus(status);
        userPolicy.setLeftcoverage(leftCoverage);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);

        assertEquals(userPolicyId, userPolicy.getUserPolicyId());
        assertEquals(coverage, userPolicy.getCoverage());
        assertEquals(term, userPolicy.getTerm());
        assertEquals(premium, userPolicy.getPremium());
        assertEquals(premiumTerm, userPolicy.getPremiumTerm());
        assertEquals(premiumCount, userPolicy.getPremiumCount());
        assertEquals(startDate, userPolicy.getStartDate());
        assertEquals(endDate, userPolicy.getEndDate());
        assertEquals(status, userPolicy.getStatus());
        assertEquals(leftCoverage, userPolicy.getLeftcoverage());
        assertEquals(user, userPolicy.getUser());
        assertEquals(policy, userPolicy.getPolicy());
    }

    @Test
    void testNoArgsConstructor() {
        UserPolicy userPolicy = new UserPolicy();
        assertNotNull(userPolicy);
    }

    @Test
    void testAllArgsConstructor() {
        Long userPolicyId = 1L;
        float coverage = 0.85f;
        int term = 12;
        float premium = 5000.0f;
        String premiumTerm = "Yearly";
        int premiumCount = 1;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        String status = "Active";
        float leftCoverage = 0.6f;
        User user = new User();
        Policy policy = new Policy();

        UserPolicy userPolicy = new UserPolicy(userPolicyId, coverage, term, premium, premiumTerm, premiumCount,
                startDate, endDate, status, leftCoverage, user, policy);

        assertEquals(userPolicyId, userPolicy.getUserPolicyId());
        assertEquals(coverage, userPolicy.getCoverage());
        assertEquals(term, userPolicy.getTerm());
        assertEquals(premium, userPolicy.getPremium());
        assertEquals(premiumTerm, userPolicy.getPremiumTerm());
        assertEquals(premiumCount, userPolicy.getPremiumCount());
        assertEquals(startDate, userPolicy.getStartDate());
        assertEquals(endDate, userPolicy.getEndDate());
        assertEquals(status, userPolicy.getStatus());
        assertEquals(leftCoverage, userPolicy.getLeftcoverage());
        assertEquals(user, userPolicy.getUser());
        assertEquals(policy, userPolicy.getPolicy());
    }
}

