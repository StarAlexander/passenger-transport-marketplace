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
            "WHERE r.departureTime BETWEEN :start AND :end " +
            "AND r.id NOT IN (" +
            "SELECT b.route.id FROM Booking b " +
            "WHERE b.user.id = :userId" +
            ")")
    List<Route> findByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("userId") Long userId);

    List<Route> findByTransportType(TransportType transportType);

    @Query("SELECT r.origin, r.destination, COUNT(b) AS bookingCount " +
            "FROM Route r " +
            "LEFT JOIN r.bookings b " +
            "GROUP BY r.origin, r.destination " +
            "ORDER BY bookingCount DESC")
    List<Object[]> findPopularRoutes();
    
    @Query("SELECT r FROM Route r " +
           "WHERE (:origin IS NULL OR r.origin = :origin) " +
           "AND (:destination IS NULL OR r.destination = :destination) " +
            "AND (:transportType is null  OR r.transportType = :transportType) " +
           "AND (:start IS NULL OR r.departureTime BETWEEN :start AND :end) " +
           "AND r.id NOT IN (" +
           "SELECT b.route.id FROM Booking b WHERE b.user.id = :userId)")
    List<Route> findAvailableRoutesByCriteriaOptional(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("transportType") TransportType transportType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("userId") Long userId
    );

    @Query("SELECT r FROM Route r " +
           "WHERE (:origin IS NULL OR r.origin = :origin) " +
           "AND (:destination IS NULL OR r.destination = :destination) " +
           "AND (:start IS NULL OR r.departureTime BETWEEN :start AND :end)")
    List<Route> findByOriginAndDestinationAndDepartureTimeBetweenOptional(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("userId") Long userId
    );


    @Query("SELECT r FROM Route r " +
            "WHERE r.origin = :origin " +
            "AND r.destination = :destination " +
            "AND (:transportType is null  OR r.transportType = :transportType) " +
            "AND r.departureTime BETWEEN :start AND :end ")
    List<Route> findDirectRoutes(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("transportType") TransportType transportType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT r FROM Route r " +
            "WHERE r.origin = :origin " +
            "AND r.departureTime BETWEEN :start AND :end " +
            "AND (:transportType is null OR r.transportType = :transportType) ")
    List<Route> findConnectingRoutes(
            @Param("origin") String origin,
            @Param("transportType") TransportType transportType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    @Query("SELECT r FROM Route r " +
            "WHERE r.origin = :origin " +
            "AND r.departureTime BETWEEN :start AND :end " +
            "AND (:transportType is null OR r.transportType = :transportType) ")
    List<Route> findOutgoingRoutes(
            @Param("origin") String origin,
            @Param("transportType") TransportType transportType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
