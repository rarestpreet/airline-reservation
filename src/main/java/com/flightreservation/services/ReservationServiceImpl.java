package com.flightreservation.services;

import com.flightreservation.model.Flight;
import com.flightreservation.model.Passenger;
import com.flightreservation.model.Reservation;
import com.flightreservation.dto.ReservationRequest;
import com.flightreservation.exceptions.FlightNotFoundException;
import com.flightreservation.repositories.FlightRepository;
import com.flightreservation.repositories.PassengerRepository;
import com.flightreservation.repositories.ReservationRepository;
import com.flightreservation.util.EmailUtil;
import com.flightreservation.util.PdfGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class ReservationServiceImpl implements ReservationService {

    @Value("${com.flightreservation.flightreservation.itinerary.dirpath}")
    private String ITINERARY_DIR;

    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final ReservationRepository reservationRepository;
    private final PdfGenerator pdfGenerator = new PdfGenerator();
    private final EmailUtil emailUtil = new EmailUtil();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    public ReservationServiceImpl(
            FlightRepository flightRepository,
            PassengerRepository passengerRepository,
            ReservationRepository reservationRepository
    ) {
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
    }


    @Override
    public Reservation bookFlight(ReservationRequest reservationRequest) {

        LOGGER.info("Inside bookFlight()");

        Long flightId=reservationRequest.getFlightId();
        Flight flight=flightRepository.findById(flightId)
                .orElseThrow(()-> new FlightNotFoundException("Flight not found"));
        if (flight==null) {
            throw new FlightNotFoundException("No flight found with id "+flightId);
        }

        LOGGER.info("Flight found with id: {}", flightId);

        Passenger passenger=new Passenger();
        passenger.setFirstName(reservationRequest.getPassengerFirstName());
        passenger.setMiddleName(reservationRequest.getPassengerMiddleName());
        passenger.setLastName(reservationRequest.getPassengerLastName());
        passenger.setEmail(reservationRequest.getPassengerEmail());
        passenger.setPhone(reservationRequest.getPassengerPhone());

        passengerRepository.save(passenger);
        LOGGER.info("Saved the passenger: {}", passenger);

        Reservation reservation=new Reservation();
        reservation.setFlight(flight);
        reservation.setPassenger(passenger);
        reservation.setCheckedIn(false);
        final Reservation savedReservation = reservationRepository.save(reservation);
        LOGGER.info("Saving the reservation: {}", reservation);

        String filePath = ITINERARY_DIR + savedReservation.getId() + ".pdf";
        LOGGER.info("Generating  the itinerary");
        pdfGenerator.generateItinerary(savedReservation,filePath);
        LOGGER.info("Emailing the Itinerary");
        emailUtil.sendItinerary("arpit.2327it1065@gmail.com",filePath);

        return savedReservation;

    }
}
