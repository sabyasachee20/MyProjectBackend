package com.example.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.EmailService;
import com.example.model.User;
import com.example.model.UserPolicy;
import com.example.service.EmailScheduler;
import com.example.service.UserPolicyService;

@ExtendWith(MockitoExtension.class)
public class EmailSchedulerTest {

    @Mock
    private UserPolicyService userPolicyService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailScheduler emailScheduler;

    private List<UserPolicy> testUserPolicies;

    @BeforeEach
    void setUp() {
        testUserPolicies = new ArrayList<>();
        User user1 = new User();
        user1.setEmailId("user1@example.com");
        user1.setFirstName("John");

        UserPolicy userPolicy1 = new UserPolicy();
        userPolicy1.setUserPolicyId(1L);
        userPolicy1.setUser(user1);
        userPolicy1.setStartDate(LocalDate.now().minusDays(30)); // Start date 30 days ago
        userPolicy1.setEndDate(LocalDate.now().plusDays(1)); // End date tomorrow

        User user2 = new User();
        user2.setEmailId("user2@example.com");
        user2.setFirstName("Jane");

        UserPolicy userPolicy2 = new UserPolicy();
        userPolicy2.setUserPolicyId(2L);
        userPolicy2.setUser(user2);
        userPolicy2.setStartDate(LocalDate.now().minusDays(60)); // Start date 60 days ago
        userPolicy2.setEndDate(LocalDate.now().plusDays(1)); // End date tomorrow

        testUserPolicies.add(userPolicy1);
        testUserPolicies.add(userPolicy2);
    }

    @Test
    void testSendEmailsBeforeEndDate() {

        when(userPolicyService.findPoliciesEndingTomorrow()).thenReturn(testUserPolicies);

        emailScheduler.sendEmailsBeforeEndDate();

        for (UserPolicy userPolicy : testUserPolicies) {
            verify(emailService, times(1)).sendSimpleMessage(
                    eq(userPolicy.getUser().getEmailId()),
                    eq("Reminder: Your policy is expiring soon"),
                    contains("Your policy is expiring tomorrow. Please renew it soon."));
        }

        verify(userPolicyService, times(1)).findPoliciesEndingTomorrow();
        verifyNoMoreInteractions(userPolicyService);
        verifyNoMoreInteractions(emailService);
    }
}

