package com.example.project.repositories;

import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByRoute(Route route);
    Booking findByUserAndRoute(User user, Route route);
}
