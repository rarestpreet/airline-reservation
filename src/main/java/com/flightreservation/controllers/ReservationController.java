package com.flightreservation.controllers;

import com.flightreservation.model.Flight;
import com.flightreservation.model.Reservation;
import com.flightreservation.dto.ReservationRequest;
import com.flightreservation.exceptions.FlightNotFoundException;
import com.flightreservation.repositories.FlightRepository;
import com.flightreservation.services.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    private final FlightRepository flightRepository;
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(FlightRepository flightRepository,
                                 ReservationService reservationService) {
        this.flightRepository = flightRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Flight> getFlightForReservation(@PathVariable Long flightId) {
        LOGGER.info("Getting flight for reservation: {}", flightId);

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));

        return ResponseEntity.ok(flight);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> completeReservation(
            @RequestBody ReservationRequest reservationRequest) {

        LOGGER.info("Completing reservation: {}", reservationRequest);

        Map<String, Object> response = new HashMap<>();

        try {
            Reservation reservation = reservationService.bookFlight(reservationRequest);

            response.put("success", true);
            response.put("message", "Reservation created successfully");
            response.put("reservationId", reservation.getId());
            response.put("reservation", reservation);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (FlightNotFoundException e) {
            response.put("success", false);
            response.put("message", "Flight not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            LOGGER.error("Error completing reservation", e);
            response.put("success", false);
            response.put("message", "Reservation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}