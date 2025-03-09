package com.example.project;

import com.example.project.models.RegisterRequest;
import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.repositories.BookingRepository;
import com.example.project.repositories.RouteRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.services.BookingService;
import com.example.project.services.RouteService;
import com.example.project.services.UserService;
import com.example.project.utils.BookingDataGenerator;
import com.example.project.utils.RouteDataGenerator;
import com.example.project.utils.UserDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

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
		var users = UserDataGenerator.generateUsers(100);
		var routes = RouteDataGenerator.generateRoutes(100);
		for (var u: users) {
			userService.registerUser(new RegisterRequest(u.getUsername(),u.getEmail(),u.getPassword()));
		}
		routeRepository.saveAll(routes);
		var bookings = BookingDataGenerator.generateBookings(userRepository.findAll(),routeRepository.findAll(),100);
		bookingRepository.saveAll(bookings);
		System.out.println(users);
	}

}
