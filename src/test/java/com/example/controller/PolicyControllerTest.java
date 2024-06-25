package com.example.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.model.Policy;
import com.example.service.PolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PolicyService policyService;

    @InjectMocks
    private PolicyController policyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(policyController).build();
    }

    @Test
    void testGetAllPolicies() throws Exception {
        List<Policy> policyList = Arrays.asList(
                new Policy(1L, "Health Insurance", "Health insurance policy description", "Terms and conditions"),
                new Policy(2L, "Car Insurance", "Car insurance policy description", "Terms and conditions"));

        when(policyService.getAllPolicies()).thenReturn(policyList);

        mockMvc.perform(get("/policies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(policyList.size()))
                .andExpect(jsonPath("$[0].policyId").value(policyList.get(0).getPolicyId()))
                .andExpect(jsonPath("$[1].policyId").value(policyList.get(1).getPolicyId()));

        verify(policyService, times(1)).getAllPolicies();
    }

    @Test
    void testGetPolicyById() throws Exception {
        Long policyId = 1L;
        Policy policy = new Policy(policyId, "Health Insurance", "Health insurance policy description", "Terms and conditions");

        when(policyService.getPolicyById(policyId)).thenReturn(Optional.of(policy));

        mockMvc.perform(get("/policies/{policyId}", policyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyId").value(policy.getPolicyId()))
                .andExpect(jsonPath("$.policyName").value(policy.getPolicyName()))
                .andExpect(jsonPath("$.policyDescription").value(policy.getPolicyDescription()));

        verify(policyService, times(1)).getPolicyById(policyId);
    }

    @Test
    void testGetPolicyById_NotFound() throws Exception {
        Long policyId = 999L;

        when(policyService.getPolicyById(policyId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/policies/{policyId}", policyId))
                .andExpect(status().isNotFound());

        verify(policyService, times(1)).getPolicyById(policyId);
    }
}
