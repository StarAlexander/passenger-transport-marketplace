package com.example.project.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
