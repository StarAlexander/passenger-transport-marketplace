
package com.example.project.repositories;

import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.User;
import com.example.project.utils.BookingDataGenerator;
import com.example.project.utils.RouteDataGenerator;
import com.example.project.utils.UserDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    private List<User> testUsers;
    private List<Route> testRoutes;
    private List<Booking> testBookings;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        routeRepository.deleteAll();
        bookingRepository.deleteAll();
        testUsers = UserDataGenerator.generateUsers(10);
        testRoutes = RouteDataGenerator.generateRoutes(10);
        userRepository.saveAll(testUsers);
        routeRepository.saveAll(testRoutes);

        testBookings = BookingDataGenerator.generateBookings(userRepository.findAll(), routeRepository.findAll(), 10);
        bookingRepository.saveAll(testBookings);
    }

    @Test
    void testFindAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        assertEquals(100, bookings.size());
    }

    @Test
    void testFindByUser() {
        var user = testUsers.get(0);
        List<Booking> bookings = bookingRepository.findByUser(user);
        assertEquals(10, bookings.size());
    }

    @Test
    void testFindByRoute() {
        var route = testRoutes.get(0);
        List<Booking> bookings = bookingRepository.findByRoute(route);
        assertEquals(10, bookings.size());
    }

    @Test
    void testDeleteBooking() {
        Long id = testBookings.get(0).getId();
        bookingRepository.deleteById(id);
        assertEquals(99, bookingRepository.findAll().size());
    }
}