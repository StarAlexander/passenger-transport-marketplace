package com.example.project.services;

import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.models.User;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.RouteRepository;
import com.example.project.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private BookingRepository bookingRepository;



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
    void testCreateBooking() throws Exception {

        Long userId = 1L;
        Long routeId = 1L;

        // Создаем моки для пользователя и маршрута
        User user = new User("testUser", "test@example.com", "password");
        Route route = new Route("A","B", LocalDateTime.now(), LocalDateTime.now().plusHours(1), TransportType.TRAIN);
        var booking = new Booking();
        booking.setRoute(route);
        booking.setBookingTime(LocalDateTime.now());
        booking.setUser(user);
        var bs = List.of(booking);
        // Настройка заглушек
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(bookingRepository.saveAll(any())).thenReturn(bs);

        // Вызываем асинхронный метод
        CompletableFuture<List<Booking>> futureBooking = bookingService.createBooking(userId, List.of(routeId));

        // Дожидаемся завершения асинхронной операции
        bs = futureBooking.get(); // Используем .get() для получения результата

        // Проверяем результат
        assertNotNull(bs);
        assertEquals(user, bs.get(0).getUser());
        assertEquals(route, bs.get(0).getRoute());

        // Проверяем, что метод save был вызван один раз
        verify(bookingRepository, times(1)).saveAll(any());
    }

    @Test
    void testCancelBooking() {
        Long bookingId = 1L;
        doNothing().when(bookingRepository).deleteById(bookingId);

        bookingService.cancelBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }
}