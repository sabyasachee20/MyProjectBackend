package com.example.service;

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

import com.example.model.User;
import com.example.repo.UserRepo;
import com.example.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepoMock;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "1234567890", "john.doe@example.com", "123 Street Ave", "password", "user", LocalDate.now());
        user2 = new User(2L, "Jane", "Smith", LocalDate.of(1995, 5, 15), "9876543210", "jane.smith@example.com", "456 Avenue St", "password123", "user", LocalDate.now());
    }

    @Test
    void testCreateUser() {
        when(userRepoMock.save(user1)).thenReturn(user1);

        User createdUser = userService.createUser(user1);

        assertEquals(user1, createdUser);
        verify(userRepoMock, times(1)).save(user1);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepoMock.findAll()).thenReturn(userList);

        List<User> retrievedUsers = userService.getAllUsers();

        assertEquals(2, retrievedUsers.size());
        assertEquals(userList, retrievedUsers);
        verify(userRepoMock, times(1)).findAll();
    }

    @Test
    void testLogin_Successful() {
        when(userRepoMock.findByEmailIdAndPassword(user1.getEmailId(), user1.getPassword())).thenReturn(user1);

        User loggedInUser = userService.login(user1.getEmailId(), user1.getPassword());

        assertEquals(user1, loggedInUser);
        verify(userRepoMock, times(1)).findByEmailIdAndPassword(user1.getEmailId(), user1.getPassword());
    }

    @Test
    void testLogin_Unsuccessful_InvalidCredentials() {
        when(userRepoMock.findByEmailIdAndPassword(user1.getEmailId(), user1.getPassword())).thenReturn(null);

        User loggedInUser = userService.login(user1.getEmailId(), user1.getPassword());

        assertNull(loggedInUser);
        verify(userRepoMock, times(1)).findByEmailIdAndPassword(user1.getEmailId(), user1.getPassword());
    }
}
