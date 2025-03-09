package com.example.project.utils;

import com.example.project.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDataGenerator {

    public static List<User> generateUsers(int count) {
        var users = new ArrayList<User>();
        for (int i = 1; i<= count; i++) {
            users.add(new User(
                    "user" + i,
                    "user" + i + "@gmail.com",
                    UUID.randomUUID().toString().substring(0,8)
            ));
        }
        return users;
    }
}
