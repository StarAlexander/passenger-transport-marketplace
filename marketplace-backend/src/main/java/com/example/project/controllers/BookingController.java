package com.example.project.controllers;


import com.example.project.models.Booking;
import com.example.project.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public CompletableFuture<ResponseEntity<List<Booking>>> createBooking(@RequestParam Long userId, @RequestParam List<Long> routeIds) {
        return bookingService.createBooking(userId, routeIds)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{bookingId}")
    public CompletableFuture<ResponseEntity<Object>> cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId)
                .thenApply(v -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<Booking>>> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
