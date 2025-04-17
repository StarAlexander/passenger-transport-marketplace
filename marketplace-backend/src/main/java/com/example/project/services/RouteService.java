package com.example.project.services;

import com.example.project.models.Route;
import com.example.project.models.RouteResponse;
import com.example.project.models.TransportType;
import com.example.project.repositories.RouteRepository;
import com.example.project.utils.Graph;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@CacheConfig(cacheNames = "routes")
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;




    @Async
    public CompletableFuture<List<RouteResponse>> searchRoutes(
            String origin,
            String destination,
            String transportType,
            LocalDateTime departureTime,
            Long userId) {
        try {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (departureTime != null) {
            start = departureTime.minusHours(2);
            end = departureTime.plusHours(2);
        }

        List<Route> routes;

        // Если транспортный тип не указан или равен "mixed", выполняем поиск без фильтрации по типу
        if ("mixed".equalsIgnoreCase(transportType)) {
            routes = routeRepository.findByOriginAndDestinationAndDepartureTimeBetweenOptional(
                    origin, destination, start, end, userId
            );
        }

        // Поиск с учетом всех параметров
        else routes = routeRepository.findAvailableRoutesByCriteriaOptional(
                origin, destination, TransportType.fromString(transportType), start, end, userId
        );
        return CompletableFuture.completedFuture(routes.stream().map(RouteResponse::fromRoute).toList());
    }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<List<RouteResponse>> findOptimalRoute(String origin, String destination, String transportType, LocalDateTime desiredDepartureTime) throws Exception {
        // Найти все доступные маршруты из начального города
        List<RouteResponse> directRoutes = searchRoutes(origin, destination, transportType, desiredDepartureTime,0L).get();

        if (!directRoutes.isEmpty()) {
            return CompletableFuture.completedFuture(directRoutes);
        }

        var graph = new Graph();

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (desiredDepartureTime != null) {
            start = desiredDepartureTime.minusHours(10);
            end = desiredDepartureTime.plusHours(10);
        }

        var allRoutes = routeRepository.findAvailableRoutesByCriteriaOptional(
                null,
                null,
                transportType.equalsIgnoreCase("mixed") ? null : TransportType.fromString(transportType),
                start,
                end,
                0L
        );

        if (allRoutes.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        for (Route route : allRoutes) {
            int travelTime = (int) Duration.between(route.getDepartureTime(), route.getArrivalTime()).toMinutes();
            graph.addEdge(route.getOrigin(), route.getDestination(), travelTime);
        }


        // Находим кратчайший путь
        List<List<String>> optimalPaths = graph.findAllPaths(origin,destination);
        return CompletableFuture.completedFuture(optimalPaths.stream().sorted(Comparator.comparingInt(List::size))
                .map(p -> convertPathsToRoutes(p,transportType.equalsIgnoreCase("mixed") ? null : TransportType.fromString(transportType),desiredDepartureTime))
                .map(RouteResponse::fromRouteList)
                .toList());
    }


    private List<Route> convertPathsToRoutes(List<String> path, TransportType transportType, LocalDateTime desiredDepartureTime) {
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            LocalDateTime prevEnd = null;
            if (!routes.isEmpty()) {
                prevEnd = routes.get(routes.size()-1).getArrivalTime();
            }
            // Находим подходящий маршрут для каждого этапа пути
            List<Route> availableRoutes = routeRepository.findAvailableRoutesByCriteriaOptional(
                    from,
                    to,
                    transportType,
                    prevEnd == null ? desiredDepartureTime.minusHours(10) : prevEnd,
                    prevEnd == null ? desiredDepartureTime.plusHours(10): prevEnd.plusHours(4),
                    0L
            );

            if (availableRoutes.isEmpty()) {
                return Collections.emptyList(); // Если этап недоступен, возвращаем пустой список
            }

            // Выбираем оптимальный маршрут для текущего этапа
            Route optimalRoute = availableRoutes.stream()
                    .min(Comparator.comparing(r -> Duration.between(r.getDepartureTime(), r.getArrivalTime())))
                    .orElse(null);

            routes.add(optimalRoute);
        }

        return routes;
    }

    @Cacheable(key = "#departureTime")
    @Async
    public CompletableFuture<List<RouteResponse>> getRoutesByDate(LocalDateTime departureTime, Long userId) {
        LocalDateTime start = departureTime.minusHours(2);
        LocalDateTime end = departureTime.plusHours(2);
        return CompletableFuture.completedFuture(routeRepository.findByDate(start,end,userId)
                .stream().map(RouteResponse::fromRoute).toList());
    }

    @Async
    public CompletableFuture<List<Object[]>> getPopularRoutes() {
        return CompletableFuture.completedFuture(routeRepository.findPopularRoutes());
    }

    @Async
    public CompletableFuture<List<RouteResponse>> getAllRoutes() {
        return CompletableFuture.completedFuture(routeRepository.findAll()
                .stream().map(RouteResponse::fromRoute).toList());
    }

    @Transactional
    @Async
    public CompletableFuture<Void> deleteRouteById(Long id) {
        routeRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<RouteResponse> getRouteById(Long id) {
        return CompletableFuture.completedFuture(
                RouteResponse.fromRoute(routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found"))));
    }

    @Transactional
    @Async
    public CompletableFuture<RouteResponse> createRoute(String origin, String destination, TransportType transportType, LocalDateTime departureTime, LocalDateTime arrivalTime) {

        return CompletableFuture.completedFuture(
                RouteResponse
                        .fromRoute(routeRepository.save(new Route(origin,destination,departureTime,arrivalTime,transportType))));
    }

    @Async
    public CompletableFuture<Long> getTotalRoutes() {
        return CompletableFuture.completedFuture(routeRepository.count());
    }
}
