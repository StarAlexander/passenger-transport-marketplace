package com.example.project.services;

import com.example.project.models.Route;
import com.example.project.models.RouteResponse;
import com.example.project.models.TransportType;
import com.example.project.repositories.RouteRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private RouteRepository routeRepository;



    @BeforeAll
    static void setupProfile() {
        System.setProperty("spring.profiles.active", "test");
    }

    @AfterAll
    static void cleanUpProfile() {
        System.clearProperty("spring.profiles.active");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindOptimalRoute_WithConnections() throws Exception {
        // Arrange
        String origin = "Moscow";
        String destination = "Berlin";
        LocalDateTime desiredDepartureTime = LocalDateTime.of(2025, 4, 10, 10, 0);

        Route route1 = new Route("Moscow", "Warsaw", desiredDepartureTime, desiredDepartureTime.plusHours(2), TransportType.TRAIN);
        Route route2 = new Route("Warsaw", "Berlin", desiredDepartureTime.plusHours(3), desiredDepartureTime.plusHours(5), TransportType.BUS);

        when(routeRepository.findAvailableRoutesByCriteriaOptional(
                null,
                null,
                null,
                desiredDepartureTime.minusHours(10),
                desiredDepartureTime.plusHours(10),
                0L
        )).thenReturn(Arrays.asList(route1, route2));

        when(routeRepository.findAvailableRoutesByCriteriaOptional(
                eq("Moscow"),
                eq("Warsaw"),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(List.of(route1));

        when(routeRepository.findAvailableRoutesByCriteriaOptional(
                eq("Warsaw"),
                eq("Berlin"),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(List.of(route2));

        // Act
        List<RouteResponse> result = routeService.findOptimalRoute(origin, destination, "mixed", desiredDepartureTime);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Ожидается один оптимальный маршрут
        assertEquals(1, result.get(0).getChildren().size()); // Маршрут состоит из двух этапов
        assertEquals("Moscow", result.get(0).getOrigin());
        assertEquals("Berlin", result.get(0).getChildren().get(0).getDestination());
    }

    @Test
    void testFindOptimalRoute_DirectRoute() throws Exception {
        // Arrange
        String origin = "Moscow";
        String destination = "Berlin";
        LocalDateTime desiredDepartureTime = LocalDateTime.of(2025, 4, 10, 10, 0);

        Route directRoute = new Route(origin, destination, desiredDepartureTime, desiredDepartureTime.plusHours(3), TransportType.AIRPLANE);

        when(routeRepository.findByOriginAndDestinationAndDepartureTimeBetweenOptional(
                origin,
                destination,
                desiredDepartureTime.minusHours(2),
                desiredDepartureTime.plusHours(2),
                0L
        )).thenReturn(List.of(directRoute));

        // Act
        List<RouteResponse> result = routeService.findOptimalRoute(origin, destination, "mixed", desiredDepartureTime);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Ожидается один маршрут
        assertEquals(0, result.get(0).getChildren().size()); // Прямой маршрут без пересадок
        assertEquals("Moscow", result.get(0).getOrigin());
        assertEquals("Berlin", result.get(0).getDestination());
    }

    @Test
    void testFindOptimalRoute_NoRoutesAvailable() throws Exception {
        // Arrange
        String origin = "Moscow";
        String destination = "Berlin";
        LocalDateTime desiredDepartureTime = LocalDateTime.of(2025, 4, 10, 10, 0);

        when(routeRepository.findAvailableRoutesByCriteriaOptional(
                eq(origin),
                eq(destination),
                any(),
                any(),
                any(),
                anyLong()
        )).thenReturn(List.of());

        // Act
        List<RouteResponse> result = routeService.findOptimalRoute(origin, destination, "mixed", desiredDepartureTime);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Нет доступных маршрутов
    }

    @Test
    void testSearchRoutes() {
        String origin = "Moscow";
        String destination = "Berlin";
        String transportType = "train";
        LocalDateTime departureTime = LocalDateTime.now();
        Long userId = 1L;

        Route route = new Route("A","B", departureTime, departureTime.plusHours(2), TransportType.TRAIN);
        List<Route> routes = List.of(route);

        when(routeRepository.findAvailableRoutesByCriteriaOptional(origin, destination, TransportType.fromString(transportType),
                departureTime.minusHours(2), departureTime.plusHours(2), userId)).thenReturn(routes);

        var res = routeService.searchRoutes(origin, destination, transportType, departureTime, userId);

        var result = res.join();

        assertEquals(1, result.size());
        assertEquals(RouteResponse.fromRoute(route), result.get(0));
    }
}