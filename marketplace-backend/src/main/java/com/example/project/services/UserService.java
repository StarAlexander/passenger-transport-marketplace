package com.example.project.services;

import com.example.project.models.RegisterRequest;
import com.example.project.models.User;
import com.example.project.repositories.UserRepository;
import com.example.project.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public User registerUser(RegisterRequest user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        return userRepository.save(new User(user.getUsername(),user.getEmail(),hashedPassword));
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!PasswordHasher.verifyPassword(password,user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
