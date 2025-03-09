package com.example.project.utils;

import com.example.project.models.Booking;
import com.example.project.models.Route;
import com.example.project.models.User;

import java.util.ArrayList;
import java.util.List;

public class BookingDataGenerator {

    public static List<Booking> generateBookings(List<User> users, List<Route> routes, int count) {
        var bookings = new ArrayList<Booking>();
        for (int i = 1; i<=count; i++) {
            var user = users.get((i - 1) % users.size());
            for (Route value : routes) {
                bookings.add(new Booking(user,value));
            }
        }

        return bookings;
    }
}
