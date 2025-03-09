package com.example.project.models;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;


    public User() {

    }
    public User(String username,String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
