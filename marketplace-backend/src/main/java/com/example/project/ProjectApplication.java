package com.example.project;

import com.example.project.models.Booking;
import com.example.project.models.RegisterRequest;
import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.RouteRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.services.UserService;
import com.example.project.utils.RouteDataGenerator;
import com.example.project.utils.UserDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {


	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private BookingRepository bookingRepository;


	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (!"test".equals(System.getProperty("spring.profiles.active"))) {


			userService.registerUser(new RegisterRequest("admin","admin@example.com","Admin123!@#")).join();




			// Для тестирования поиска оптимального маршрута

			Route route1 = new Route(
					"London",
					"Atlanta",
					LocalDateTime.of(2025, 5, 20, 14, 30),
					LocalDateTime.of(2025, 5, 20, 18, 30),
					TransportType.AIRPLANE
			);

			Route route2 = new Route(
					"Atlanta",
					"New York",
					LocalDateTime.of(2025, 5, 20, 19, 30),
					LocalDateTime.of(2025, 5, 20, 22, 30),
					TransportType.TRAIN
			);


			Route route4 = new Route(
					"London",
					"Toronto",
					LocalDateTime.of(2025, 5, 27, 14, 30), // Время отправления
					LocalDateTime.of(2025, 5, 27, 17, 30), // Время прибытия
					TransportType.AIRPLANE
			);
			Route route5 = new Route(
					"Toronto",
					"Buffalo",
					LocalDateTime.of(2025, 5, 27, 17, 50), // Время отправления
					LocalDateTime.of(2025, 5, 27, 20, 0), // Время прибытия
					TransportType.TRAIN
			);

			Route route6 = new Route(
					"Buffalo",
					"New York",
					LocalDateTime.of(2025, 5, 27, 21, 30), // Время отправления
					LocalDateTime.of(2025, 5, 27, 22, 30), // Время прибытия
					TransportType.BUS
			);
			routeRepository.saveAll(List.of(route4,route5,route6));

			// --------------------------------------


			routeRepository.saveAll(RouteDataGenerator.generateRoutes(10));
		}
	}
}
