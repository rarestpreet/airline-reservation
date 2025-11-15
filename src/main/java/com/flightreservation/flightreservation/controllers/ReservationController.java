package com.flightreservation.flightreservation.controllers;

import com.flightreservation.flightreservation.model.Flight;
import com.flightreservation.flightreservation.model.Reservation;
import com.flightreservation.flightreservation.dto.ReservationRequest;
import com.flightreservation.flightreservation.exceptions.FlightNotFoundException;
import com.flightreservation.flightreservation.repositories.FlightRepository;
import com.flightreservation.flightreservation.services.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final FlightRepository flightRepository;
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(FlightRepository flightRepository, ReservationService reservationService) {
        this.flightRepository = flightRepository;
        this.reservationService = reservationService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    @GetMapping("/showCompleteReservation")
    public String showCompleteReservation(@RequestParam("flightId") Long flightId, ModelMap modelMap) {
        LOGGER.info("showCompleteReservation() invoked with the Flight Id: {}", flightId);
        Flight flight = flightRepository.findById(flightId)
                        .orElseThrow(()-> new FlightNotFoundException("Flight not found"));

        LOGGER.info("Flight found: {}", flight);
        modelMap.addAttribute("flight", flight);

        return "reservation/completeReservation";
    }

    @PostMapping("/completeReservation")
    public String completeReservation(ReservationRequest reservationRequest, ModelMap modelMap) {

        LOGGER.info("completeReservation() invoked with the Reservation: {}", reservationRequest.toString());
        Reservation reservation = reservationService.bookFlight(reservationRequest);
        modelMap.addAttribute("msg",
                "Reservation created successfully and the reservation id is " +
                reservation.getId());

        return "reservation/reservationConfirmation";
    }

}
