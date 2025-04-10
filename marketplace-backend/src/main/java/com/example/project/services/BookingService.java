package com.example.project.services;

import com.example.project.models.Booking;
import com.example.project.models.User;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.RouteRepository;
import com.example.project.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;


    @Transactional
    @Async
    public CompletableFuture<List<Booking>> createBooking(Long userId, List<Long> routeIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var routes = routeRepository.findAllById(routeIds);
        var bs = new ArrayList<Booking>();
        for (var r: routes) {
            var b = bookingRepository.findByRouteAndUser(r,user);
            if (b.isEmpty()) {
                bs.add(new Booking(user,r));
            }
        }

        return CompletableFuture.completedFuture(bookingRepository.saveAll(bs));
    }

    @Transactional
    @Async
    public CompletableFuture<Void> cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<Double> getAverageVisitsPerUser() {
        long totalUsers = bookingRepository.countDistinctUsers();
        long totalBookings = bookingRepository.count();

        if (totalUsers == 0) {
            return CompletableFuture.completedFuture(0.0); // Защита от деления на ноль
        }

        return CompletableFuture.completedFuture((double) totalBookings / totalUsers);
    }




    // Распределение бронирований по типам транспорта
    @Async
    public CompletableFuture<List<Object[]>> getBookingsByTransportType() {
        return CompletableFuture.completedFuture(bookingRepository.getBookingsByTransportType());
    }

    @Async
    public CompletableFuture<List<Booking>> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return CompletableFuture.completedFuture(bookingRepository.findByUser(user));
    }
}
