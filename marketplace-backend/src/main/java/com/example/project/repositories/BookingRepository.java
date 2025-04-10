package com.example.project.repositories;

import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);
    List<Booking> findByRoute(Route route);

    @Query("SELECT COUNT(DISTINCT b.user.id) FROM Booking b")
    long countDistinctUsers();

    @Query("SELECT COUNT(b) FROM Booking b")
    long count();

    @Query("SELECT r.transportType, COUNT(b) AS bookingCount " +
            "FROM Booking b " +
            "JOIN b.route r " +
            "GROUP BY r.transportType")
    List<Object[]> getBookingsByTransportType();

    Optional<Booking> findByRouteAndUser(Route route, User user);
}
