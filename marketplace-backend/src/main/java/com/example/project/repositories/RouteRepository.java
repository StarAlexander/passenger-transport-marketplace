package com.example.project.repositories;

import com.example.project.models.Route;
import com.example.project.models.TransportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("SELECT r FROM Route r " +
            "WHERE r.origin = :origin " +
            "AND r.destination = :destination " +
            "AND r.transportType = :transportType " +
            "AND r.departureTime BETWEEN :start AND :end " +
            "AND r.id NOT IN (" +
            "SELECT b.route.id FROM Booking b " +
            "WHERE b.user.id = :userId" +
            ")")
    List<Route> findAvailableRoutesByCriteria(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("transportType") TransportType transportType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("userId") Long userId
    );

    @Query("SELECT r FROM Route r " +
            "WHERE r.departureTime BETWEEN :start AND :end " +
            "AND r.id NOT IN (" +
            "SELECT b.route.id FROM Booking b " +
            "WHERE b.user.id = :userId" +
            ")")
    List<Route> findByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("userId") Long userId);

    List<Route> findByTransportType(TransportType transportType);

    List<Route> findByOriginAndDestinationAndDepartureTimeBetween(String origin, String destination, LocalDateTime start, LocalDateTime end);
}
