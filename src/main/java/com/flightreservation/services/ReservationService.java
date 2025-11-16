package com.flightreservation.services;

import com.flightreservation.model.Reservation;
import com.flightreservation.dto.ReservationRequest;


public interface ReservationService {
    public Reservation bookFlight(ReservationRequest reservationRequest);
}
