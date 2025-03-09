package com.example.project.repositories;


import com.example.project.models.Route;
import com.example.project.utils.RouteDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RouteRepositoryTest {


    @Autowired
    private RouteRepository routeRepository;

    private List<Route>  routes = RouteDataGenerator.generateRoutes(30);

    @BeforeEach
    void setUpRoutes(){
        routeRepository.deleteAll();
        routeRepository.saveAll(routes);
    }


    @Test
    void testFindAllRoutes() {
        var routes = routeRepository.findAll();
        assertEquals(30,routes.size());
    }

    @Test
    void testFindByTransportType() {
        var type = routes.get(0).getTransportType();
        var routes = routeRepository.findByTransportType(type);

        System.out.println(routeRepository.findAll().size());
        assertEquals(10,routes.size());
    }

}
