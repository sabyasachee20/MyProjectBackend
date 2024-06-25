package com.example.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        Long userId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dob = LocalDate.of(1990, 5, 15);
        String phoneNo = "1234567890";
        String emailId = "john.doe@example.com";
        String address = "123 Street, City";
        String password = "password";
        String role = "user";
        LocalDate createdAt = LocalDate.now();

        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDob(dob);
        user.setPhoneNo(phoneNo);
        user.setEmailId(emailId);
        user.setAddress(address);
        user.setPassword(password);
        user.setRole(role);
        user.setCreatedAt(createdAt);

        assertEquals(userId, user.getUserId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(dob, user.getDob());
        assertEquals(phoneNo, user.getPhoneNo());
        assertEquals(emailId, user.getEmailId());
        assertEquals(address, user.getAddress());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testAllArgsConstructor() {
        Long userId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dob = LocalDate.of(1990, 5, 15);
        String phoneNo = "1234567890";
        String emailId = "john.doe@example.com";
        String address = "123 Street, City";
        String password = "password";
        String role = "user";
        LocalDate createdAt = LocalDate.now();

        User user = new User(userId, firstName, lastName, dob, phoneNo, emailId, address, password, role, createdAt);

        assertEquals(userId, user.getUserId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(dob, user.getDob());
        assertEquals(phoneNo, user.getPhoneNo());
        assertEquals(emailId, user.getEmailId());
        assertEquals(address, user.getAddress());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(createdAt, user.getCreatedAt());
    }
}
