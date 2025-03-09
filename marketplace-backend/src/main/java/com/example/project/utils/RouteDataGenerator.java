package com.example.project.utils;

import com.example.project.models.Route;
import com.example.project.models.TransportType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RouteDataGenerator {

    public static List<Route> generateRoutes(int count) {
        List<Route> routes = new ArrayList<>();
        String[] origins = { "Moscow", "Saint Petersburg", "Berlin", "Paris", "London", "Rome", "Madrid", "Barcelona", "Tokyo", "Osaka" };

        for (int i = 1; i <= count; i++) {
            String origin = origins[i % origins.length];
            String destination = origins[(i + 1) % origins.length];
            LocalDateTime departureTime = LocalDateTime.now().plusDays(i).plusHours((i % 24));
            LocalDateTime arrivalTime = departureTime.plusHours(2);
            TransportType transportType = TransportType.values()[i % TransportType.values().length];
            Route route = new Route(origin, destination, departureTime, arrivalTime, transportType);
            routes.add(route);
        }

        return routes;
    }
}
