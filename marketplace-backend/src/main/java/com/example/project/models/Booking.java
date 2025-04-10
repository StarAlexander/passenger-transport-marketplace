package com.example.project.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="route_id", nullable=false)
    private Route route;
    private LocalDateTime bookingTime = LocalDateTime.now();

    public Booking() {

    }

    public Booking(User user, Route route) {
        this.user = user;
        this.route = route;
    }

}
