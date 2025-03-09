package com.example.project.services;

import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> searchRoutes(String origin, String destination, String transportType, LocalDateTime departureTime,Long userId) {
        LocalDateTime start = departureTime.minusHours(2);
        LocalDateTime end = departureTime.plusHours(2);
        if ("mixed".equalsIgnoreCase(transportType))
              return routeRepository.findByOriginAndDestinationAndDepartureTimeBetween(origin,destination,start,end);
        return routeRepository.findAvailableRoutesByCriteria(origin,destination,TransportType.fromString(transportType),start,end,userId);
    }

    public List<Route> getRoutesByDate(LocalDateTime departureTime, Long userId) {
        LocalDateTime start = departureTime.minusHours(2);
        LocalDateTime end = departureTime.plusHours(2);
        return routeRepository.findByDate(start,end,userId);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
    public void deleteRouteById(Long id) {
        routeRepository.deleteById(id);
    }
    public Route getRouteById(Long id) {
        return routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public Route createRoute(String origin, String destination, String transportType, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return routeRepository.save(new Route(origin,destination,departureTime,arrivalTime,TransportType.fromString(transportType)));
    }
}
