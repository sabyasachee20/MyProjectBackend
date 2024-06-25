package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.model.UserPolicy;
import com.example.service.UserPolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserPolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserPolicyService userPolicyService;

    @InjectMocks
    private UserPolicyController userPolicyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userPolicyController).build();
    }

    @Test
    void testGetAllUserPolicies() throws Exception {
        List<UserPolicy> userPolicyList = Arrays.asList(
                new UserPolicy(1L, 1000.0f, 1, 500.0f, "Monthly", 0, LocalDate.now(), LocalDate.now().plusYears(1), "Active", 500.0f, null, null),
                new UserPolicy(2L, 2000.0f, 2, 1000.0f, "Yearly", 0, LocalDate.now(), LocalDate.now().plusYears(2), "Active", 1000.0f, null, null));

        when(userPolicyService.getAllUserPolicies()).thenReturn(userPolicyList);

        mockMvc.perform(get("/user-policies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userPolicyList.size()))
                .andExpect(jsonPath("$[0].userPolicyId").value(userPolicyList.get(0).getUserPolicyId()))
                .andExpect(jsonPath("$[1].userPolicyId").value(userPolicyList.get(1).getUserPolicyId()));

        verify(userPolicyService, times(1)).getAllUserPolicies();
    }

    @Test
    void testReadOne() throws Exception {
        Long userPolicyId = 1L;
        UserPolicy userPolicy = new UserPolicy(userPolicyId, 1000.0f, 1, 500.0f, "Monthly", 0, LocalDate.now(), LocalDate.now().plusYears(1), "Active", 500.0f, null, null);

        when(userPolicyService.readOne(userPolicyId)).thenReturn(userPolicy);

        mockMvc.perform(get("/user-policies/readOne/{id}", userPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPolicyId").value(userPolicy.getUserPolicyId()))
                .andExpect(jsonPath("$.coverage").value(userPolicy.getCoverage()))
                .andExpect(jsonPath("$.term").value(userPolicy.getTerm()));

        verify(userPolicyService, times(1)).readOne(userPolicyId);
    }

}

