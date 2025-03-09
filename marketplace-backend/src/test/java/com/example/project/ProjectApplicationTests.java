package com.example.project;

import com.example.project.controllers.BookingController;
import com.example.project.controllers.RouteController;
import com.example.project.models.Route;
import com.example.project.models.TransportType;
import com.example.project.services.BookingService;
import com.example.project.services.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class ProjectApplicationTests {

	@InjectMocks
	private RouteController routeController;

	@Mock
	private RouteService routeService;

	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSearchRoutesByDate(){
		var departureTime = LocalDateTime.now();
		Long userId = 1L;

		var route1 = new Route("Moscow", "Saint Petersburg", LocalDateTime.now(), LocalDateTime.now().plusHours(2), TransportType.fromString("airplane"));
		var route2 = new Route("Berlin", "Paris", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), TransportType.fromString("train"));
		Mockito.when(routeService.getRoutesByDate(departureTime,userId)).thenReturn(Arrays.asList(route1,route2));
		var routes = routeController.getRoutesByDate(departureTime,userId);
		assertEquals(2,routes.size());
		Mockito.verify(routeService,Mockito.times(1)).getRoutesByDate(departureTime,userId);


	}

}
