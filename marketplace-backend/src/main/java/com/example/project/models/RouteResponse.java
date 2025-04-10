package com.example.project.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class RouteResponse {
    private Long id;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private TransportType transportType;
    private List<Route> children = new ArrayList<>();

    public RouteResponse(Long id, String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, TransportType transportType) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.transportType = transportType;
    }

    public RouteResponse(Long id, String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, TransportType transportType, List<Route> children) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.transportType = transportType;
        this.children = children;
    }


    public static RouteResponse fromRoute(Route r) {
        return new RouteResponse(
                r.getId(),
                r.getOrigin(),
                r.getDestination(),
                r.getDepartureTime(),
                r.getArrivalTime(),
                r.getTransportType()
        );
    }

    public static RouteResponse fromRouteList(List<Route> rs) {
        if (rs.isEmpty()) return null;
        var first = rs.get(0);
        return new RouteResponse(
                first.getId(),
                first.getOrigin(),
                first.getDestination(),
                first.getDepartureTime(),
                first.getArrivalTime(),
                first.getTransportType(),
                rs.subList(1,rs.size())
        );
    }
}
