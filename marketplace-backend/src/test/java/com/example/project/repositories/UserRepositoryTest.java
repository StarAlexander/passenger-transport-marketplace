package com.example.project.repositories;


import com.example.project.models.User;
import com.example.project.utils.UserDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private List<User> users;

    @BeforeEach
    void setUp(){
        users = UserDataGenerator.generateUsers(30);
        userRepository.saveAll(users);
    }

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }


    @Test
    void testFindAllUsers() {
        var users = userRepository.findAll();
        assertEquals(30,users.size());
    }


    @Test
    void testFindByEmail(){
        String email = users.get(0).getEmail();
        var user = userRepository.findByEmail(email).orElse(new User());
        assertEquals(email,user.getEmail());
    }

    @Test
    void testDeleteUser(){
        var id = users.get(0).getId();
        userRepository.deleteById(id);
        assertEquals(29,userRepository.findAll().size());
    }
}
