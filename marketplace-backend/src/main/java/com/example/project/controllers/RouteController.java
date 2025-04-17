package com.example.project.controllers;

import com.example.project.models.RouteResponse;
import com.example.project.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<RouteResponse>>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(defaultValue = "mixed") String transportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
            @RequestParam Long userId) {
        return routeService.searchRoutes(origin, destination,transportType, departureTime,userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @GetMapping("/optimal")
    public CompletableFuture<ResponseEntity<List<RouteResponse>>> findOptimalRoute(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "mixed") String transportType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desiredDepartureTime) throws Exception {
        return routeService.findOptimalRoute(origin, destination,transportType, desiredDepartureTime)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<RouteResponse>>> getAllRoutes() {
        return routeService.getAllRoutes()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<RouteResponse>> getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/by-date")
    public CompletableFuture<ResponseEntity<List<RouteResponse>>> getRoutesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                                       @RequestParam Long userId) {
        return routeService.getRoutesByDate(departureTime,userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Object>> deleteRouteById(@PathVariable Long id) {
        return routeService.deleteRouteById(id)
                .thenApply(v -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
