package com.example.project.models;

public enum TransportType {
    AIRPLANE("airplane"),
    TRAIN("train"),
    BUS("bus");

    private final String text;

    TransportType(final String text) {
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }

    public static TransportType fromString(String text) {
        for (TransportType b : TransportType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
