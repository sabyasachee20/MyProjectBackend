package com.example.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.EmailService;
import com.example.model.UserPolicy;

import java.util.List;

@Component
public class EmailScheduler {

    private final UserPolicyService userPolicyService;
    private final EmailService emailService;

    public EmailScheduler(UserPolicyService userPolicyService, EmailService emailService) {
        this.userPolicyService = userPolicyService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 32 22 * * *")
    public void sendEmailsBeforeEndDate() {
        executeEmailSendingTask();
    }

//    @EventListener(ContextRefreshedEvent.class)
//    public void sendEmailsBeforeEndDate() {
//        executeEmailSendingTask();
//    }

    public void executeEmailSendingTask() {
        List<UserPolicy> userPolicies = userPolicyService.findPoliciesEndingTomorrow();

        for (UserPolicy userPolicy : userPolicies) {
            String email = userPolicy.getUser().getEmailId();
            String subject = "Reminder: Your policy is expiring soon";
            String message = "Dear " + userPolicy.getUser().getFirstName() + ",\n"
                    + "Your policy is expiring tomorrow. Please renew it soon.";

            emailService.sendSimpleMessage(email, subject, message);
        }
    }
}
