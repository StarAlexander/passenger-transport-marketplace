package com.example.project.controllers;

import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/search")
    public List<Route> searchRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "mixed") String transportType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
            @RequestParam Long userId) {

        System.out.println(origin);
        System.out.println(destination);
        System.out.println(transportType);
        return routeService.searchRoutes(origin, destination,transportType, departureTime,userId);
    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("/{id}")
    public Route getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id);
    }

    @GetMapping("/by-date")
    public List<Route> getRoutesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                                       @RequestParam Long userId) {
        return routeService.getRoutesByDate(departureTime,userId);
    }


    @DeleteMapping("/{id}")
    public void deleteRouteById(@PathVariable Long id) {
        routeService.deleteRouteById(id);
    }
}
