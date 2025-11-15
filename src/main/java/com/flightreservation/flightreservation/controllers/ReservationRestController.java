package com.flightreservation.flightreservation.controllers;


import com.flightreservation.flightreservation.model.Reservation;
import com.flightreservation.flightreservation.dto.ReservationUpdateRequest;
import com.flightreservation.flightreservation.exceptions.ReservationNotFoundException;
import com.flightreservation.flightreservation.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationRestController {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationRestController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRestController.class);

    @GetMapping("/reservations/{id}")
    public Reservation findReservation(@PathVariable("id") Long id) {

        LOGGER.info("Inside findReservation() for id: {}", id);

        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
    }


    @PostMapping(value = "/reservations")
    public Reservation updateReservation(@RequestBody ReservationUpdateRequest reservationUpdateRequest) {

        Reservation reservation = reservationRepository.findById(reservationUpdateRequest.getId())
                        .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        reservation.setNumberOfBags(reservationUpdateRequest.getNumberOfBags());
        reservation.setCheckedIn(reservationUpdateRequest.isCheckedIn());

        return reservationRepository.save(reservation);
    }
}
