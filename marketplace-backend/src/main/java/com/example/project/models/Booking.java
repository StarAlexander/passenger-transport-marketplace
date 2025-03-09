package com.example.project.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Route route;
    private LocalDateTime bookingTime = LocalDateTime.now();

    public Booking() {

    }

    public Booking(User user, Route route) {
        this.user = user;
        this.route = route;
    }

}
