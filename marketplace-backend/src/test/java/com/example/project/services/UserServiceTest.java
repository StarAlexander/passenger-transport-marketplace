package com.example.project.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.project.models.RegisterRequest;
import com.example.project.models.User;
import com.example.project.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @BeforeAll
    static void setupProfile() {
        System.setProperty("spring.profiles.active", "test");
    }

    @AfterAll
    static void cleanUpProfile() {
        System.clearProperty("spring.profiles.active");
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String username = "testUser";
    private final String email = "test@example.com";
    private final String password = "password123";
    private final String hashedPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());


    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // Присваиваем ID для имитации сохранения
            return user;
        });

        // Act
        var res = userService.registerUser(registerRequest);
        var registeredUser = res.join();
        // Assert
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        assertTrue(BCrypt.verifyer().verify(password.toCharArray(),hashedPassword).verified);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        User existingUser = new User(username, email, hashedPassword);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registerRequest);
        });
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testLoginUser_Success() {
        // Arrange
        User user = new User(username, email, hashedPassword);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        var res = userService.loginUser(email, password);

        var loggedInUser = res.join();
        // Assert
        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals(email, loggedInUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }




    @Test
    void testGetUserById_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User(username, email, hashedPassword);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        var res = userService.getUserById(userId);
        var foundUser = res.join();
        // Assert
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}