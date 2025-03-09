package com.example.project.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private TransportType transportType; // "airplane", "train", "bus"

    public Route() {

    }

    public Route(String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, TransportType transportType) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.transportType = transportType;
    }
}