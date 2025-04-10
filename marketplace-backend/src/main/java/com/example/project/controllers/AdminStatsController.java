package com.example.project.controllers;

import com.example.project.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin/stats")
public class AdminStatsController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/average-visits")
    public CompletableFuture<ResponseEntity<Double>> getAverageVisits() {
        return bookingService.getAverageVisitsPerUser()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}