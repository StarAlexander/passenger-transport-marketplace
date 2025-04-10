package com.example.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private TransportType transportType;

    @OneToMany(mappedBy = "route")
    @JsonIgnore
    private List<Booking> bookings;

    public Route() {}

    public Route(String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, TransportType transportType) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.transportType = transportType;
    }
}