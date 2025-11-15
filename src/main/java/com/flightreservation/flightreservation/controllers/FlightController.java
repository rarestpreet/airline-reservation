package com.flightreservation.flightreservation.controllers;

import com.flightreservation.flightreservation.model.Flight;
import com.flightreservation.flightreservation.repositories.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class FlightController {

    private static final Logger LOGGER= LoggerFactory.getLogger(FlightController.class);

    private final FlightRepository flightRepository;

    @Autowired
    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping("/findFlights")
    public String findFLights(@RequestParam("source") String source, @RequestParam("destination") String destination,
                              @RequestParam("departDate") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate departDate,
                              ModelMap modelMap) {

        LOGGER.info("Inside findFlights() From: " + source + " TO: " + destination + "Departure Date: " + departDate);
        List<Flight> flights =flightRepository.findFlights(source,destination,departDate);
        modelMap.addAttribute("flights",flights);
        LOGGER.info("Flights found are {}", flights);
        return "flights/displayFlights";
    }

    @GetMapping("/admin/showAddFlight")
    public String showAddFlightPage(){
        return "flights/addFlight";
    }

    @PostMapping("/admin/addFlight")
    public String addFlight(@ModelAttribute("flight") Flight flight, ModelMap modelmap){
       flightRepository.save(flight);
       modelmap.addAttribute("msg","Flight Added Successfully");
       return "flights/addFlight";
    }

}
