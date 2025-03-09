package com.example.project.services;
import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.User;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.RouteRepository;
import com.example.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    public Booking createBooking(Long userId, Long routeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoute(route);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public List<Booking> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user);
    }
}
