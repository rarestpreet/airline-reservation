package com.flightreservation.flightreservation.services;

import com.flightreservation.flightreservation.model.Reservation;
import com.flightreservation.flightreservation.dto.ReservationRequest;


public interface ReservationService {
    public Reservation bookFlight(ReservationRequest reservationRequest);
}
