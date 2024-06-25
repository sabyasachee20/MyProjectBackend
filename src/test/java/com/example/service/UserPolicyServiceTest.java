package com.example.service;

import com.example.model.Policy;
import com.example.model.User;
import com.example.model.UserPolicy;
import com.example.repo.UserPolicyRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPolicyServiceTest {

    @Mock
    private UserPolicyRepo userPolicyRepository;

    @InjectMocks
    private UserPolicyService userPolicyService;

    private UserPolicy userPolicy;
    private User user;
    private Policy policy;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        policy = new Policy();
        policy.setPolicyId(1L);

        userPolicy = new UserPolicy();
        userPolicy.setUserPolicyId(1L);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setPremium(5000);
        userPolicy.setStartDate(LocalDate.of(2020, 1, 1));
        userPolicy.setEndDate(LocalDate.of(2025, 1, 1));
        userPolicy.setCoverage(10000);
        userPolicy.setTerm(5);
        userPolicy.setPremiumCount(1);
    }

    @Test
    void createUserPolicy_shouldCreateUserPolicy_whenValid() {
        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(userPolicy);

        UserPolicy createdUserPolicy = userPolicyService.createUserPolicy(userPolicy);

        assertNotNull(createdUserPolicy);
        assertEquals(5000, createdUserPolicy.getLeftcoverage());
        verify(userPolicyRepository, times(1)).save(userPolicy);
    }

    @Test
    void createUserPolicy_shouldThrowException_whenUserIsNotPersisted() {
        user.setUserId(null);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userPolicyService.createUserPolicy(userPolicy);
        });

        assertEquals("User must be persisted before associating with UserPolicy", exception.getMessage());
    }

    @Test
    void createUserPolicy_shouldThrowException_whenPolicyIsNotPersisted() {
        policy.setPolicyId(null);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userPolicyService.createUserPolicy(userPolicy);
        });

        assertEquals("Policy must be persisted before associating with UserPolicy", exception.getMessage());
    }

    @Test
    void getAllUserPolicies_shouldReturnAllUserPolicies() {
        List<UserPolicy> policies = Arrays.asList(userPolicy);
        when(userPolicyRepository.findAll()).thenReturn(policies);

        List<UserPolicy> result = userPolicyService.getAllUserPolicies();

        assertEquals(1, result.size());
        verify(userPolicyRepository, times(1)).findAll();
    }

    @Test
    void getUserPolicyById_shouldReturnUserPolicy_whenFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        Optional<UserPolicy> result = userPolicyService.getUserPolicyById(1L);

        assertTrue(result.isPresent());
        assertEquals(userPolicy, result.get());
        verify(userPolicyRepository, times(1)).findById(1L);
    }

    @Test
    void getUserPolicyById_shouldReturnEmpty_whenNotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserPolicy> result = userPolicyService.getUserPolicyById(1L);

        assertFalse(result.isPresent());
        verify(userPolicyRepository, times(1)).findById(1L);
    }

    @Test
    void readOne_shouldReturnUserPolicy_whenFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        UserPolicy result = userPolicyService.readOne(1L);

        assertNotNull(result);
        assertEquals(userPolicy, result);
        verify(userPolicyRepository, times(1)).findById(1L);
    }

    @Test
    void readOne_shouldThrowException_whenNotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userPolicyService.readOne(1L);
        });

        assertEquals("Invalid account ID", exception.getMessage());
    }



    @Test
    void renew_shouldThrowException_whenPolicyNotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userPolicyService.renew(1L);
        });

        assertEquals("Invalid account ID", exception.getMessage());
    }

    @Test
    void renew_shouldThrowException_whenRenewalDateNotValid() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));
        userPolicy.setEndDate(LocalDate.now().minusDays(1));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userPolicyService.renew(1L);
        });

        assertEquals("Renewal is not possible after the policy end date", exception.getMessage());
    }

    @Test
    void renew_shouldThrowException_whenNotLastServingDay() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));
        userPolicy.setEndDate(LocalDate.now().plusDays(1));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userPolicyService.renew(1L);
        });

        assertEquals("Renewal is only possible on Last Policy Serving Day. Come on " + userPolicy.getEndDate() + " to renew your policy", exception.getMessage());
    }

    @Test
    void findPoliciesEndingTomorrow_shouldReturnPoliciesEndingTomorrow() {
        List<UserPolicy> policies = Arrays.asList(userPolicy);
        when(userPolicyRepository.findByEndDate(LocalDate.now().plusDays(1))).thenReturn(policies);

        List<UserPolicy> result = userPolicyService.findPoliciesEndingTomorrow();

        assertEquals(1, result.size());
        assertEquals(userPolicy, result.get(0));
        verify(userPolicyRepository, times(1)).findByEndDate(LocalDate.now().plusDays(1));
    }
}
