package com.example.project.controllers;

import com.example.project.services.BookingService;
import com.example.project.services.RouteService;
import com.example.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/total-users")
    public CompletableFuture<ResponseEntity<Long>> getTotalUsers() {
        return userService.getTotalUsers()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/total-routes")
    public CompletableFuture<ResponseEntity<Long>> getTotalRoutes() {
        return routeService.getTotalRoutes()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/average-bookings-per-user")
    public CompletableFuture<ResponseEntity<Double>> getAverageBookingsPerUser() {
        return userService.getAverageBookingsPerUser()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/popular-routes")
    public CompletableFuture<ResponseEntity<List<Object[]>>> getPopularRoutes() {
        return routeService.getPopularRoutes()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/bookings-by-transport-type")
    public CompletableFuture<ResponseEntity<List<Object[]>>> getBookingsByTransportType() {
        return bookingService.getBookingsByTransportType()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}