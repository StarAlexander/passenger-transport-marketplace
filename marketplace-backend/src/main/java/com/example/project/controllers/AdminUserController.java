package com.example.project.controllers;

import com.example.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{userId}/activate")
    public CompletableFuture<ResponseEntity<Object>> activateUser(@PathVariable Long userId) {
        return userService.activateUser(userId)
                .thenApply(v -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{userId}/deactivate")
    public CompletableFuture<ResponseEntity<Object>> deactivateUser(@PathVariable Long userId) {
        return userService.deactivateUser(userId)
                .thenApply(v -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}