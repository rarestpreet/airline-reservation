package com.flightreservation.flightreservation.exceptions;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String exception) {
        super(exception);
    }
}
