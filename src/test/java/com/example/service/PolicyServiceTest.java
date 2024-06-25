package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.model.Policy;
import com.example.repo.PolicyRepo;
import com.example.service.PolicyService;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {

    @Mock
    private PolicyRepo policyRepoMock;

    @InjectMocks
    private PolicyService policyService;

    private Policy policy1;
    private Policy policy2;

    @BeforeEach
    void setUp() {
        policy1 = new Policy(1L, "Policy A", "Description A", "Terms A");
        policy2 = new Policy(2L, "Policy B", "Description B", "Terms B");
    }

    @Test
    void testCreatePolicy() {
        when(policyRepoMock.save(policy1)).thenReturn(policy1);

        Policy createdPolicy = policyService.createPolicy(policy1);

        assertEquals(policy1, createdPolicy);
        verify(policyRepoMock, times(1)).save(policy1);
    }

    @Test
    void testGetAllPolicies() {
        List<Policy> policyList = Arrays.asList(policy1, policy2);
        when(policyRepoMock.findAll()).thenReturn(policyList);

        List<Policy> retrievedPolicies = policyService.getAllPolicies();

        assertEquals(2, retrievedPolicies.size());
        assertEquals(policyList, retrievedPolicies);
        verify(policyRepoMock, times(1)).findAll();
    }

    @Test
    void testGetPolicyById() {
        when(policyRepoMock.findById(policy1.getPolicyId())).thenReturn(Optional.of(policy1));

        Optional<Policy> retrievedPolicyOpt = policyService.getPolicyById(policy1.getPolicyId());

        assertTrue(retrievedPolicyOpt.isPresent());
        assertEquals(policy1, retrievedPolicyOpt.get());
        verify(policyRepoMock, times(1)).findById(policy1.getPolicyId());
    }

    @Test
    void testGetPolicyById_NotFound() {
        Long nonExistentPolicyId = 100L;
        when(policyRepoMock.findById(nonExistentPolicyId)).thenReturn(Optional.empty());

        Optional<Policy> retrievedPolicyOpt = policyService.getPolicyById(nonExistentPolicyId);

        assertFalse(retrievedPolicyOpt.isPresent());
        verify(policyRepoMock, times(1)).findById(nonExistentPolicyId);
    }
}

