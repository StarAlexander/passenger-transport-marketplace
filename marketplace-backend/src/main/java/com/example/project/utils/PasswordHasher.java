package com.example.project.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12,password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(),hashedPassword).verified;
    }
}
