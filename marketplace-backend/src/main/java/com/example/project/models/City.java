package com.example.project.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "origin")
    private List<Route> outgoingRoutes;

    public City() {}

    public City(String name) {
        this.name = name;
    }
}

