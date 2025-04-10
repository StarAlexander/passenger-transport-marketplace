package com.example.project.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private String username;
    private String email;
    private String password;
    private Boolean active = true;


    @OneToMany(mappedBy = "user",orphanRemoval = true)
    @JsonIgnore
    private Set<Booking> bookings;

    public User() {

    }
    public User(String username,String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
