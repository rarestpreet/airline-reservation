package com.flightreservation.controllers;

import com.flightreservation.model.Flight;
import com.flightreservation.repositories.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);
    private final FlightRepository flightRepository;

    @Autowired
    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> findFlights(
            @RequestParam("source") String source,
            @RequestParam("destination") String destination,
            @RequestParam("departDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departDate) {

        LOGGER.info("Inside findFlights() From: {} TO: {} Departure Date: {}", source, destination, departDate);

        try {
            List<Flight> flights = flightRepository.findFlights(source, destination, departDate);
            LOGGER.info("Flights found: {}", flights.size());
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            LOGGER.error("Error finding flights", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        LOGGER.info("Getting flight with id: {}", id);
        return flightRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        LOGGER.info("Adding flight: {}", flight);
        try {
            Flight savedFlight = flightRepository.save(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
        } catch (Exception e) {
            LOGGER.error("Error adding flight", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        LOGGER.info("Getting all flights");
        List<Flight> flights = flightRepository.findAll();
        return ResponseEntity.ok(flights);
    }
}