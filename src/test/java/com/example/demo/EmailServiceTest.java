package com.example.demo;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

//import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.EmailService;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mockMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendSimpleMessage() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Hello, this is a test message.";

        doNothing().when(mockMailSender).send(any(SimpleMailMessage.class));

        emailService.sendSimpleMessage(to, subject, text);
        verify(mockMailSender, times(1)).send(argThat((SimpleMailMessage message) ->
                message.getTo()[0].equals(to) &&
                message.getSubject().equals(subject) &&
                message.getText().equals(text)));
        verifyNoMoreInteractions(mockMailSender);
    }
}
