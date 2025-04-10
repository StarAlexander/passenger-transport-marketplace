package com.example.project.services;

import com.example.project.models.RegisterRequest;
import com.example.project.models.User;
import com.example.project.models.UserResponse;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.utils.PasswordHasher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BookingRepository bookingRepository;


    @Async
    @Transactional
    public CompletableFuture<UserResponse> registerUser(RegisterRequest user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        var userCreate = userRepository.save(new User(user.getUsername(),user.getEmail(),hashedPassword));
        return CompletableFuture.completedFuture(
                new UserResponse(userCreate.getId(), userCreate.getUsername(), userCreate.getEmail(), userCreate.getActive())
        );
    }

    @Async
    @Transactional
    public CompletableFuture<UserResponse> loginUser(String email, String password) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getActive()) {
                throw new RuntimeException("User is inactive");
            }
            if (!PasswordHasher.verifyPassword(password,user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            return CompletableFuture.completedFuture(
                    new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getActive())
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

    }

    @Transactional
    @Async
    public CompletableFuture<Void> activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
        return CompletableFuture.completedFuture(null);
    }


    @Transactional
    @Async
    public CompletableFuture<Void> deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return CompletableFuture.completedFuture(null);
    }


    @Async
    @Cacheable(key="#id")
    public CompletableFuture<UserResponse> getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return CompletableFuture.completedFuture(
                new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getActive())
        );
    }



    @Async
    public CompletableFuture<Double> getAverageBookingsPerUser() {
        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();

        if (totalUsers == 0) {
            return CompletableFuture.completedFuture(0.0);
        }

        return CompletableFuture.completedFuture((double) totalBookings / totalUsers);
    }


    @Async
    public CompletableFuture<List<User>> getUsers() {

        return CompletableFuture.completedFuture(userRepository.findAll());
    }

    @Async
    public CompletableFuture<Long> getTotalUsers() {
        return CompletableFuture.completedFuture(userRepository.count());
    }
}
