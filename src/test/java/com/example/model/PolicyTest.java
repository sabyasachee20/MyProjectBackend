package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PolicyTest {

    @InjectMocks
    private Policy policy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long policyId = 1L;
        String policyName = "Health Insurance";
        String policyDescription = "Comprehensive health coverage";
        String termsAndConditions = "Terms and conditions apply";

        policy.setPolicyId(policyId);
        policy.setPolicyName(policyName);
        policy.setPolicyDescription(policyDescription);
        policy.setTermsAndConditons(termsAndConditions);

        assertEquals(policyId, policy.getPolicyId());
        assertEquals(policyName, policy.getPolicyName());
        assertEquals(policyDescription, policy.getPolicyDescription());
        assertEquals(termsAndConditions, policy.getTermsAndConditons());
    }

    @Test
    void testNoArgsConstructor() {
        Policy policy = new Policy();
        assertNotNull(policy);
    }

    @Test
    void testAllArgsConstructor() {
        Long policyId = 1L;
        String policyName = "Health Insurance";
        String policyDescription = "Comprehensive health coverage";
        String termsAndConditions = "Terms and conditions apply";

        Policy policy = new Policy(policyId, policyName, policyDescription, termsAndConditions);

        assertEquals(policyId, policy.getPolicyId());
        assertEquals(policyName, policy.getPolicyName());
        assertEquals(policyDescription, policy.getPolicyDescription());
        assertEquals(termsAndConditions, policy.getTermsAndConditons());
    }
}

