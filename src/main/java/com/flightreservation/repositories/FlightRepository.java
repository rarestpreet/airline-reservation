package com.flightreservation.repositories;

import com.flightreservation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,Long>{

    @Query("FROM Flight WHERE departureCity=:departureCity AND arrivalCity=:arrivalCity AND dateOfDeparture=:dateOfDeparture")
    List<Flight> findFlights(String departureCity, String arrivalCity, LocalDate dateOfDeparture);
}
