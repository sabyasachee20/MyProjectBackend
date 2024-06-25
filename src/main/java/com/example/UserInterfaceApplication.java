package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.example.service.EmailScheduler;

@SpringBootApplication
public class UserInterfaceApplication {
	  private final EmailScheduler emailScheduler;

	    public UserInterfaceApplication(EmailScheduler emailScheduler) {
	        this.emailScheduler = emailScheduler;
	    }

	public static void main(String[] args) {
		SpringApplication.run(UserInterfaceApplication.class, args);
	}
	@EventListener(ApplicationReadyEvent.class)
	public void sendMail() {
		emailScheduler.executeEmailSendingTask();
	}

}
