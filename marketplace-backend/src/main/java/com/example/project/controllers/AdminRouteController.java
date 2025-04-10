package com.example.project.controllers;

import com.example.project.models.RouteCreate;
import com.example.project.models.RouteResponse;
import com.example.project.models.TransportType;
import com.example.project.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin/routes")
public class AdminRouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<RouteResponse>> addRoute(@RequestBody RouteCreate routeRequest) {
        return routeService.createRoute(
                routeRequest.getOrigin(),
                routeRequest.getDestination(),
                TransportType.fromString(routeRequest.getTransportType()),
                routeRequest.getDepartureTime(),
                routeRequest.getArrivalTime()
        )
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Object>> deleteRoute(@PathVariable Long id) {
        return routeService.deleteRouteById(id)
                .thenApply(v -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
