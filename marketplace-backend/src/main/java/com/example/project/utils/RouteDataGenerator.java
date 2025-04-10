package com.example.project.utils;

import com.example.project.models.Route;
import com.example.project.models.TransportType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RouteDataGenerator {

    // Карта расстояний между городами (в километрах)
    private static final Map<String, Map<String, Integer>> DISTANCE_MAP = new HashMap<>();

    static {
        DISTANCE_MAP.put("Moscow", Map.of(
                "Saint Petersburg", 700,
                "Berlin", 1600,
                "London", 2500,
                "New York", 7500,
                "Toronto", 7800,
                "Madrid", 3400,
                "Rome", 2300
        ));
        DISTANCE_MAP.put("Saint Petersburg", Map.of(
                "Moscow", 700,
                "Berlin", 1300,
                "London", 2000,
                "New York", 7200
        ));
        DISTANCE_MAP.put("London", Map.of(
                "Moscow", 2500,
                "Berlin", 900,
                "New York", 5600,
                "Toronto", 5800,
                "San Francisco", 8600
        ));
        DISTANCE_MAP.put("New York", Map.of(
                "London", 5600,
                "Toronto", 800,
                "San Francisco", 4000,
                "Los Angeles", 3900
        ));
        DISTANCE_MAP.put("Toronto", Map.of(
                "New York", 800,
                "San Francisco", 3500
        ));
        DISTANCE_MAP.put("San Francisco", Map.of(
                "Los Angeles", 600
        ));
        DISTANCE_MAP.put("Los Angeles", Map.of(
                "San Francisco", 600
        ));
        DISTANCE_MAP.put("Berlin", Map.of(
                "Moscow", 1600,
                "London", 900
        ));
        DISTANCE_MAP.put("Madrid", Map.of(
                "Moscow", 3400,
                "Berlin", 1800
        ));
        DISTANCE_MAP.put("Rome", Map.of(
                "Moscow", 2300,
                "Berlin", 1100
        ));
    }

    public static List<Route> generateRoutes(int count) {
        List<Route> routes = new ArrayList<>();
        Random random = new Random();
        List<String> cities = new ArrayList<>(DISTANCE_MAP.keySet());

        for (int i = 1; i <= count; i++) {
            // Случайный выбор города отправления
            String origin = cities.get(random.nextInt(cities.size()));

            // Случайный выбор города назначения (не совпадает с отправлением)
            String destination;
            do {
                destination = cities.get(random.nextInt(cities.size()));
            } while (origin.equals(destination));

            // Проверка, существует ли маршрут между городами
            if (!DISTANCE_MAP.get(origin).containsKey(destination)) {
                continue; // Пропускаем маршрут, если расстояние не определено
            }

            int distance = DISTANCE_MAP.get(origin).get(destination);

            // Определение типа транспорта и времени в пути
            TransportType transportType;
            long travelTimeHours;

            if (distance > 5000) {
                // Дальние расстояния: только самолет
                transportType = TransportType.AIRPLANE;
                travelTimeHours = TimeUnit.MILLISECONDS.toHours((long) (distance / 800.0 * 60 * 60)); // Средняя скорость самолета ~800 км/ч
            } else if (distance > 500) {
                // Средние расстояния: поезд или автобус
                transportType = random.nextBoolean() ? TransportType.TRAIN : TransportType.BUS;
                travelTimeHours = TimeUnit.MILLISECONDS.toHours((long) (distance / 100.0 * 60 * 60)); // Средняя скорость ~100 км/ч
            } else {
                // Короткие расстояния: автобус или поезд
                transportType = TransportType.BUS;
                travelTimeHours = TimeUnit.MILLISECONDS.toHours((long) (distance / 60.0 * 60 * 60)); // Средняя скорость ~60 км/ч
            }

            // Случайное время отправления (в течение месяца)
            LocalDateTime departureTime = LocalDateTime.now()
                    .plusDays(random.nextInt(30))
                    .plusHours(random.nextInt(24))
                    .plusMinutes(random.nextInt(60));

            // Время прибытия
            LocalDateTime arrivalTime = departureTime.plusHours(travelTimeHours);

            // Создание маршрута
            Route route = new Route(origin, destination, departureTime, arrivalTime, transportType);
            routes.add(route);
        }

        return routes;
    }
}